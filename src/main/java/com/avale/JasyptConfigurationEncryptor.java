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
	public static void main(String... program_arguments) {
		launch(program_arguments);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("jce_i18n"));
		loader.setLocation(locate("views/main.fxml"));
		Parent rootUiElement = loader.load();

		primaryStage.setTitle("Jasypt Configuration Encryptor");
		URL iconUrl = locate("images/application_logo.png");
		primaryStage.getIcons().add(new Image(iconUrl.toString()));
		primaryStage.setScene(new Scene(rootUiElement));
		primaryStage.setResizable(false);
		primaryStage.show();
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
}