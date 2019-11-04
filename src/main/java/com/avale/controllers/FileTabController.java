package com.avale.controllers;

import com.avale.model.Configuration;
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

	private Configuration configuration;

	@Override
	void initialize() {
		validateWiringOf(configurationText, TextArea.class, "file content text area");
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
