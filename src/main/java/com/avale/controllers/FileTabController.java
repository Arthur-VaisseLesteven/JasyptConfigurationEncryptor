package com.avale.controllers;

import com.avale.model.Configuration;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


public class FileTabController extends Controller {
	@FXML
	TextArea textArea;
	private Configuration configuration;

	@Override
	void initialize() {
		validateWiringOf(textArea, TextArea.class, "file content text area");
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		this.textArea.setText(configuration.text());
	}
}
