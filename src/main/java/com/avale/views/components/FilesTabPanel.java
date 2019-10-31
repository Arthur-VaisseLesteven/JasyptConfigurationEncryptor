package com.avale.views.components;

import com.avale.model.ConfigurationFile;
import javafx.scene.control.TabPane;

import java.io.File;

public class FilesTabPanel extends TabPane {
	public void open(final File file) {
		this.getTabs().add(new FileTab(new ConfigurationFile(file)));
	}
}
