package com.avale.controllers;

import com.avale.model.*;
import com.avale.model.exception.BusinessException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTabController extends Controller implements ApplicationContextAwareController {
	@FXML
	private TextArea configurationText;
	@FXML
	private ChoiceBox<String> algorithm;
	@FXML
	private TextField encryptIteration;
	@FXML
	private TextField masterPassword;
	@FXML
	private TextField regexp;
	@FXML
	private CheckBox configMetadata;

	/**
	 * Model object of this controller.
	 */
	private Configuration configuration;
	/**
	 * Indicates whether the encryption settings are frozen, and should not get editable again.
	 * In case the configuration currently contains any encrypted property, then the settings are supposed to be frozen.
	 */
	private boolean encryptionSettingsAreFrozen = false;

	public FileTabController() {
		super();
	}

	@Override
    public void initialize() {
		validateWiringOf(configurationText, TextArea.class, "file content text area");
        validateWiringOf(algorithm, ChoiceBox.class, "algorithm drop down selector");
        validateWiringOf(encryptIteration, TextField.class, "input used to select the number of iteration used during encryption");
        validateWiringOf(masterPassword, TextField.class, "input used to select the password used for encryption");

        algorithm.getItems().addAll(EncryptionSettings.availablePasswordBasedEncryptionAlgorithms());
		configurationText.setEditable(false);
	}

	/** Called once the configuration to display have been loaded, whatever the way it have been loaded. */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		this.configurationText.setText(configuration.text());

		configuration.encryptionSettings().ifPresent(this::applyPreviousEncryptionSettings);
		configuration.onContentReplacement(this::applyReplacement);
	}

	/**
	 * Factory for alert windows to inform user of errors or bad usage of the software.
	 */
	public AlertFactory getAlertFactory() {
		return new AlertFactory(this.configMetadata.getScene().getWindow());
	}

	/**
	 * Apply the existing encryption settings in case some were already existing prior the edition of the configuration.
	 */
	private void applyPreviousEncryptionSettings(EncryptionSettings encryptionSettings) {
		this.encryptionSettingsAreFrozen = true; // as the configuration already contains encrypted content, we should not allow any change to encryption settings.
		algorithm.setValue(encryptionSettings.algorithm());
		encryptIteration.setText(String.valueOf(encryptionSettings.numberOfIteration()));

		lockEncryptionSettingToPreventInconsistency();
	}

	/**
	 * This method is called upon configuration content change.
	 */
	private void applyReplacement(final ConfigurationChange change) {
		this.configurationText.setText(change.finalValue);
		selectConfigurationBetween(change.startOfChangeIndex(), change.endOfChangeIndex());
	}

	/**
	 * Select the content between given index on the configuration content.
	 */
	private void selectConfigurationBetween(int bound, int otherBound) {
		this.configurationText.positionCaret(Math.min(bound, otherBound));
		this.configurationText.selectPositionCaret(Math.max(bound, otherBound));
//		this.configurationText.selectRange(Math.min(bound, otherBound), Math.max(bound, otherBound));
	}

	/**
	 * Disable edit on encryption settings to prevent user to mix different encryption settings among a single file which would then make it impossible to decipher.
	 */
	private void lockEncryptionSettingToPreventInconsistency() {
		algorithm.setDisable(true);
		encryptIteration.setDisable(true);
	}

	/**
	 * Unlock the encryption setting UI component in case the encryption settings are not frozen.
	 */
	private void unlockEncryptionSetting() {
		if (!encryptionSettingsAreFrozen) {
			algorithm.setDisable(false);
			encryptIteration.setDisable(false);
		}
	}

	@FXML
	private void encryptSelection() {
		validateInputAndThenRun(() -> {
			lockEncryptionSettingToPreventInconsistency();
			try {
				new SimpleConfigurationEncryptor().encrypt(currentSelection(), configuration, getEncryptionSettings());
			} catch (BusinessException exception) {
				unlockEncryptionSetting();
				throw exception;
			}
		}, "warning.precondition.encryption");
	}

	private boolean encryptionSettingsAreValid() {
		boolean thereIsAnAlgorithm = !algorithm.getSelectionModel().isEmpty();
		boolean thereIsAMasterPassword = !masterPassword.getText().isEmpty();
		return thereIsAnAlgorithm && thereIsAMasterPassword;
	}

	private SimpleSelection currentSelection() {
		IndexRange selection = configurationText.getSelection();
		return new SimpleSelection(selection.getStart(), selection.getEnd());
	}

	private EncryptionSettings getEncryptionSettings() {
		String rawIterations = encryptIteration.getText();
		int numberOfIteration = rawIterations.isEmpty() ? 1000 : Integer.parseInt(rawIterations);
		return new EncryptionSettings(algorithm.getValue(), masterPassword.getText(), numberOfIteration);
	}

	private void validateInputAndThenRun(Runnable process, String performedActionKey) {
		if (currentSelection().isEmpty()) {
			getAlertFactory().warnUser("warning.precondition.selection", "warning.precondition.selection.description");
		} else if (!encryptionSettingsAreValid()) {
			getAlertFactory().warnUser(performedActionKey, performedActionKey + ".description");
		} else {
			try {
				process.run();
			} catch (BusinessException exception) {
				getAlertFactory().reportError(exception);
			}
		}
	}

	@FXML
	private void decryptSelection() {
		validateInputAndThenRun(() -> new SimpleConfigurationEncryptor().decrypt(currentSelection(), configuration, getEncryptionSettings()), "warning.precondition.decryption");
	}

	void revertLastChange() {
		configuration.undoLastChange();
	}

	void redoLastRevertedChange() {
		configuration.redoLastRevertedChange();
	}

	void saveConfiguration() {
		configuration.save();
	}

	@FXML
	public void switchMetadataSaving() {
		if (configMetadata.isSelected()) {
			configuration.enable(ConfigurationFeatures.SAVE_META_DATA);
		} else {
			configuration.disable(ConfigurationFeatures.SAVE_META_DATA);
		}
	}

	@Override
	public Node getNode() {
		return this.masterPassword;
	}


	@FXML
	public boolean selectNextMatchingSequence() {
		if (!regexp.getText().isEmpty()) {
			Pattern pattern = Pattern.compile("(?m)^" + regexp.getText() + "$");
			Matcher matcher = pattern.matcher(configuration.text());
			if (matcher.groupCount() != 1) {
				getAlertFactory().warnUser("warning.precondition.regexp", "warning.precondition.regexp.description"); // todo : a definir dans le resource bundle
			}

			if (matcher.find(configurationText.getCaretPosition()) || matcher.find()) { // XXX - the select from current position or from start of the text allow to select next occurrence, using circular search and starting again from start of content once the end have been reached
				selectConfigurationBetween(matcher.start(1), matcher.end(1));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@FXML
	public void encryptAllMatchingSequences() {
		// todo find a better implementation, because this one sucks and easily trigger infinite loop... this is sad because I found it elegant at first! :')
//		while (selectNextMatchingSequence()) {
//			encryptSelection();
//		}
	}
}
