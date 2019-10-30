package com.avale.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController extends Controller {
	public MainController() {}

	/**
	 * Dependency Injection constructor.
	 * Used for testing purpose only as JavaFX uses a default constructor and then perform field injection.
	 */
	MainController(final FilesTabController filesTabController) {
		this.filesTabController = filesTabController;
	}

	@FXML
	private FilesTabController filesTabController;

	public void initialize() {
		validatesWiringOf(filesTabController, "filesTabController");
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
	private void selectFileToOpen(final ActionEvent actionEvent) {
		File file = new FileChooser().showOpenDialog(null);
		if (file != null) filesTabController.open(file);
	}

}
