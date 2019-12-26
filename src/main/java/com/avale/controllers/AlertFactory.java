package com.avale.controllers;

import com.avale.model.exception.BusinessException;
import javafx.scene.control.Alert;
import javafx.stage.Window;


public class AlertFactory implements ApplicationContextAware {

	private final Window window;

	AlertFactory(Window window) {
		this.window = window;
	}

	void reportError(BusinessException exception) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(getWindow());
		alert.setTitle(getResourceBundle().getString("error.title"));
		alert.setHeaderText(getResourceBundle().getString(exception.getMessage()));
		alert.setContentText(exception.getCause().toString());
		exception.printStackTrace();

		alert.show();
	}

	void warnUser(String messageHeaderKey, String messageContentKey) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.initOwner(getWindow());
		alert.setTitle(getResourceBundle().getString("warning.title"));
		alert.setHeaderText(getResourceBundle().getString(messageHeaderKey));
		alert.setContentText(getResourceBundle().getString(messageContentKey));

		alert.show();
	}

	@Override
	public Window getWindow() {
		return this.window;
	}
}
