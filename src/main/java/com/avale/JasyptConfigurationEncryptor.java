package com.avale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class JasyptConfigurationEncryptor extends Application {

	private static final String APPLICATION_RESOURCE_BUNDLE_NAME = "jce_i18n";
	private static final String APPLICATION_LOGO                 = "images/application_logo.png";
	private static final String MAIN_VIEW                        = "views/main.fxml";

	public static void main(String... program_arguments) {
		launch(program_arguments);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(APPLICATION_RESOURCE_BUNDLE_NAME);
		Parent rootGuiNode = loadMainView(locate(MAIN_VIEW), resourceBundle);

		configureAppDisplay(primaryStage, resourceBundle.getString("application.title"));
		primaryStage.setScene(new Scene(rootGuiNode));
		primaryStage.show();
	}

	private Parent loadMainView(URL viewLocation, ResourceBundle resourceBundle) throws java.io.IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(viewLocation);
		loader.setResources(resourceBundle);
		return loader.load();
	}

	/**
	 * @return THe URL of the located resource.
	 * @throws IllegalStateException In case the resource we were looking for was not found.
	 */
	private URL locate(final String resourceIdentifier) {
		URL resourceLocation = getClass().getClassLoader().getResource(resourceIdentifier);

		if(resourceLocation == null)throw new IllegalStateException("Failed to locate " + resourceIdentifier);

		return resourceLocation;
	}

	private void configureAppDisplay(final Stage primaryStage, final String applicationTitle) {
		URL iconUrl = locate(APPLICATION_LOGO);
		primaryStage.getIcons().add(new Image(iconUrl.toString()));
		primaryStage.setTitle(applicationTitle);
	}
}