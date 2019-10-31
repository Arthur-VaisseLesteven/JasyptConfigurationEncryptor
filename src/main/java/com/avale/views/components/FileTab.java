package com.avale.views.components;

import com.avale.JasyptConfigurationEncryptor;
import com.avale.ResourceLocator;
import com.avale.model.Configuration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;

import java.io.IOException;
import java.util.ResourceBundle;

class FileTab extends Tab {
	public static final String        FILES_TAB_FXML = "views/filesTab.fxml";
	private final       Configuration configuration;

	FileTab(final Configuration configuration) {
		super();
		loadNodeHierarchy();
		this.configuration = configuration;
	}

	private void loadNodeHierarchy() {
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle(JasyptConfigurationEncryptor.APPLICATION_RESOURCE_BUNDLE_NAME));
		loader.setLocation(new ResourceLocator().locate(FILES_TAB_FXML));

		try {
			Node view = loader.load();
			this.setContent(view);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
}
