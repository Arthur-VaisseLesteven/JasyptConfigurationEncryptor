package com.avale.views.components;

import com.avale.model.ConfigurationFile;
import javafx.scene.control.TabPane;

import java.io.File;
import java.util.Optional;

public class FilesTabPanel extends TabPane {
	public void open(final File file) {
		select(tabDisplaying(file).orElseGet(() -> openFileTab(file)));
	}

	private void select(FileTab fileTab) {
		this.getSelectionModel().select(fileTab);
	}

	private Optional<FileTab> tabDisplaying(File file) {
		return this.getTabs().stream().map(FileTab.class::cast).filter(tab -> tab.contains(file)).findAny();
	}

	private FileTab openFileTab(File file) {
		FileTab fileTab = new FileTab(new ConfigurationFile(file));
		this.getTabs().add(fileTab);
		return fileTab;
	}
}
