package com.avale.views.components;

import com.avale.JasyptConfigurationEncryptor;
import com.avale.ResourceLocator;
import com.avale.controllers.FileTabController;
import com.avale.model.ConfigurationFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

class FileTab extends Tab {
	private static final String FILES_TAB_FXML = "views/filesTab.fxml";

	private final ConfigurationFile configuration;

	FileTab(final ConfigurationFile configuration) {
		super();
		this.configuration = configuration;

		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle(JasyptConfigurationEncryptor.APPLICATION_RESOURCE_BUNDLE_NAME));
		loader.setLocation(new ResourceLocator().locate(FILES_TAB_FXML));

		Node view;
		try {
			view = loader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.setContent(view);
		this.setText(configuration.name());
		FileTabController controller = loader.getController();
		controller.setConfiguration(configuration);
	}

	public boolean contains(File file) {
		return this.configuration.isTiedTo(file);
	}
}
