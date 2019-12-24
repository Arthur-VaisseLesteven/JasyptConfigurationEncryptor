package com.avale.controllers;

import com.avale.JasyptConfigurationEncryptor;
import com.avale.model.*;
import com.avale.model.exception.BusinessException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ResourceBundle;


public class FileTabController extends Controller {
	@FXML
	private TextArea configurationText;
	@FXML
	private ChoiceBox<String> algorithm;
	@FXML
	private TextField encryptIteration;
	@FXML
	private TextField masterPassword;
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

	/**
	 * Dependency Injection Constructor aiming at easing testing.
	 */
	protected FileTabController(TextArea configurationText, ChoiceBox<String> algorithm, TextField encryptIteration, TextField masterPassword) {

	}

	@Override
    public void initialize() {
		validateWiringOf(configurationText, TextArea.class, "file content text area");
        validateWiringOf(algorithm, ChoiceBox.class, "algorithm drop down selector");
        validateWiringOf(encryptIteration, TextField.class, "input used to select the number of iteration used during encryption");
        validateWiringOf(masterPassword, TextField.class, "input used to select the password used for encryption");

        algorithm.getItems().addAll(EncryptionSettings.availablePasswordBasedEncryptionAlgorithms());
		configurationText.setEditable(false);
		// TODO : make password not clearly displayed. Maybe use a formatter ?
	}

	/** Called once the configuration to display have been loaded, whatever the way it have been loaded. */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		this.configurationText.setText(configuration.text());

		configuration.encryptionSettings().ifPresent(this::applyPreviousEncryptionSettings);
		configuration.onContentReplacement(this::applyReplacement);
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
		if (encryptionSettingsAreValid()) {
			lockEncryptionSettingToPreventInconsistency();
			try {
				new SimpleConfigurationEncryptor().encrypt(currentSelection(), configuration, getEncryptionSettings());
			} catch (BusinessException exception) {
				unlockEncryptionSetting();
				notifyActionFailure(exception);
			}
		} else {
			//TODO gives the user a feedback !
		}
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

	private void notifyActionFailure(BusinessException exception) {
		// it really annoy me to have to us ethe singleton pattern here while the controller was initialized by a loader having a reference on the resource bundle...
		// TODO : find the proper javaFx way to perform that kind of localization...
		ResourceBundle bundle = JasyptConfigurationEncryptor.getBundle();
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(bundle.getString("error.title"));
		alert.setHeaderText(bundle.getString(exception.getMessage()));
		alert.setContentText(exception.getCause().toString());
		exception.printStackTrace();

		alert.show();
	}

	@FXML
	private void decryptSelection(final ActionEvent actionEvent) {
		if (encryptionSettingsAreValid()) {
			new SimpleConfigurationEncryptor().decrypt(currentSelection(), configuration, getEncryptionSettings());
		} else {
			// TODO gives the user a feedback
		}
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
}
