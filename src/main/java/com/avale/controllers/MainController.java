package com.avale.controllers;

import com.avale.views.components.FilesTabPanel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController extends Controller {
	public MainController() {}

	@FXML
	private FilesTabPanel filesPanel;

	public void initialize() {
		validateWiringOf(filesPanel, TabPane.class, "filesPanel");
	}

	/**
	 * Stops the main JavaFX thread and the JVM.
	 */
	@FXML
	private void closeApplication() {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	private void selectFileToOpen() {
		File file = new FileChooser().showOpenDialog(null);
		if (file != null) filesPanel.open(file);
	}

}
