package com.avale.views.components;

import com.avale.model.ConfigurationFile;
import javafx.scene.control.TabPane;

import java.io.File;

public class FilesTabPanel extends TabPane {
	public void open(final File file) {
		FileTab fileTab = new FileTab(new ConfigurationFile(file));
		this.getTabs().add(fileTab);
		this.getSelectionModel().select(fileTab);
	}
}
