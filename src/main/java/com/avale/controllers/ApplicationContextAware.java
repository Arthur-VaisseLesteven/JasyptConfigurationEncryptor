package com.avale.controllers;

import javafx.stage.Window;

import java.util.ResourceBundle;

/**
 * Is aware of the application context. Can access to the main view of the application, it's window, and can access to
 * other common settings such as the resource bundle to use for gui display.
 */
public interface ApplicationContextAware {
	/**
	 * @return The resource bundle used by the application.
	 */
	default ResourceBundle getResourceBundle() {
		return (ResourceBundle) getWindow().getProperties().get(ResourceBundle.class);
	}

	/**
	 * @return The main application view element : the {@link Window}
	 */
	Window getWindow();
}
