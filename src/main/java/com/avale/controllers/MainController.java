package com.avale.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class MainController implements Controller{

	public void initialize() {
		// Nothing to do for now.
	}

	/**
	 * Stops the main JavaFX thread and the JVM.
	 */
	@FXML
	private void closeApplication() {
		Platform.exit();
		System.exit(0);
	}
}
