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

	public static final String APPLICATION_RESOURCE_BUNDLE_NAME = "jce_i18n";
	public static final String APPLICATION_LOGO                 = "images/application_logo.png";
	public static final String MAIN_VIEW                        = "views/main.fxml";

	private static ResourceBundle RESOURCE_BUNDLE;

	public static synchronized ResourceBundle getBundle() {
		if (RESOURCE_BUNDLE == null) RESOURCE_BUNDLE = ResourceBundle.getBundle(APPLICATION_RESOURCE_BUNDLE_NAME);
		return RESOURCE_BUNDLE;
	}

	public static void main(String... program_arguments) {
		launch(program_arguments);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		ResourceBundle resourceBundle = getBundle();
		Parent         rootGuiNode    = loadMainView(new ResourceLocator().locate(MAIN_VIEW), resourceBundle);

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

	private void configureAppDisplay(final Stage primaryStage, final String applicationTitle) {
		URL iconUrl = new ResourceLocator().locate(APPLICATION_LOGO);
		primaryStage.getIcons().add(new Image(iconUrl.toString()));
		primaryStage.setTitle(applicationTitle);
	}
}