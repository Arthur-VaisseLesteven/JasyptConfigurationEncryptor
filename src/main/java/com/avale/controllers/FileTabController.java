package com.avale.controllers;

import com.avale.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class FileTabController extends Controller {
	@FXML
	TextArea          configurationText;
	@FXML
	ChoiceBox<String> algorithm;
	@FXML
	TextField         encryptIteration;
	@FXML
	TextField         masterPassword;

    /**
     * Model object of this controller.
     */
    private Configuration configuration;

    @Override
    public void initialize() {
		validateWiringOf(configurationText, TextArea.class, "file content text area");
        validateWiringOf(algorithm, ChoiceBox.class, "algorithm drop down selector");
        validateWiringOf(encryptIteration, TextField.class, "input used to select the number of iteration used during encryption");
        validateWiringOf(masterPassword, TextField.class, "input used to select the password used for encryption");

        algorithm.getItems().addAll(EncryptionSettings.availablePasswordBasedEncryptionAlgorithms());
		// TODO : make password not clearly displayed. Maybe use a formatter ?
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		this.configurationText.setText(configuration.text());

		configuration.encryptionSettings().ifPresent(this::applyPreviousEncryptionSettings);
		configuration.onContentReplacement(this::applyReplacement);
	}

	private void applyReplacement(final Replacement replacement) {
		this.configurationText.setText(replacement.applyOn(this.configurationText.getText()));
	}

	private void applyPreviousEncryptionSettings(EncryptionSettings encryptionSettings) {
		algorithm.setValue(encryptionSettings.algorithm());
		encryptIteration.setText(String.valueOf(encryptionSettings));

		preventSettingInconsistency();
	}

	/**
	 * Disable edit on encryption settings to prevent user to mix different encryption settings among a single file which would then make it impossible to decipher.
	 */
	private void preventSettingInconsistency() {
		algorithm.setDisable(true);
		encryptIteration.setDisable(true);
	}

	@FXML
	private void encryptSelection() {
		if (encryptionSettingsAreValid()) {
			preventSettingInconsistency();
			new SimpleConfigurationEncryptor().encrypt(currentSelection(), configuration, getEncryptionSettings());
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

	@FXML
	private void decryptSelection(final ActionEvent actionEvent) {

	}
}
