package com.avale.controllers;

import com.avale.views.components.FilesTabPanel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class MainController extends Controller {


	public MainController() {}

	@FXML
	private FilesTabPanel filesPanel;
	private FileChooser fileChooser = new FileChooser();

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
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		if (files != null) {
			files.forEach(filesPanel::open);
			reopenFileChooserNextTimeInDirectoryOf(files.get(0));
		}
	}

	private void reopenFileChooserNextTimeInDirectoryOf(File file) {
		fileChooser.setInitialDirectory(file.getParentFile());
	}

	@FXML
	private void save() {
		filesPanel.currentTabController().ifPresent(FileTabController::saveConfiguration);
	}

	@FXML
	private void undo() {
		filesPanel.currentTabController().ifPresent(FileTabController::revertLastChange);
	}

	@FXML
	private void redo() {
		filesPanel.currentTabController().ifPresent(FileTabController::redoLastRevertedChange);
	}
}
