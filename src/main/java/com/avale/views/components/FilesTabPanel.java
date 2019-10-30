package com.avale.views.components;

import javafx.scene.control.TabPane;

import java.io.File;

public class FilesTabPanel extends TabPane {
	public void open(final File file) {
		System.out.println(file.getAbsolutePath());
	}
}
