package com.avale.views;

import com.avale.JasyptConfigurationEncryptor;
import com.avale.model.exception.BusinessException;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;

public class AlertFactory {

	ResourceBundle resourceBundle;

	public AlertFactory(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public AlertFactory() {
		// it really annoy me to have to use the singleton pattern here while the controllers using the AlertFactory were
		// initialized by a loader having a reference on the resource bundle...
		// TODO : find the proper javaFx way to perform that kind of localization...
		this(JasyptConfigurationEncryptor.getBundle());
	}

	public void reportError(BusinessException exception) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(resourceBundle.getString("error.title"));
		alert.setHeaderText(resourceBundle.getString(exception.getMessage()));
		alert.setContentText(exception.getCause().toString());
		exception.printStackTrace();

		alert.show();
	}

	public void warnUser(String mesageHeaderKey, String messageContentKey) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(resourceBundle.getString("warning.title"));
		alert.setHeaderText(resourceBundle.getString(mesageHeaderKey));
		alert.setContentText(resourceBundle.getString(messageContentKey));

		alert.show();
	}

}
