package com.avale.views.components;

import com.avale.controllers.FileTabController;
import com.avale.model.ConfigurationFile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.File;
import java.util.Optional;

public class FilesTabPanel extends TabPane {

	public FilesTabPanel() {
		super();
	}

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

	public Optional<FileTabController> currentTabController() {
		return currentSelectedTab().map(FileTab::controller);
	}

	private Optional<FileTab> currentSelectedTab() {
		return this.getTabs().stream().filter(Tab::isSelected).findFirst().map(FileTab.class::cast);
	}
}
