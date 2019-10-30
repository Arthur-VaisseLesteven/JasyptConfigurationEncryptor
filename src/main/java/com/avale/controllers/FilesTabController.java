package com.avale.controllers;

import java.io.File;

public class FilesTabController extends Controller {
	@Override
	public void initialize() {
		// nothing to do for now.
	}

	void open(final File file) {
		System.out.println(file.getAbsolutePath());
	}
}
