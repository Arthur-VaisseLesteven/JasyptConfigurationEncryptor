package com.avale.controllers;

import com.avale.model.Configuration;
import com.avale.model.EncryptionSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


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
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		this.configurationText.setText(configuration.text());
	}

	@FXML
	private void encryptSelection(final ActionEvent actionEvent) {

	}

	@FXML
	private void decryptSelection(final ActionEvent actionEvent) {

	}
}
