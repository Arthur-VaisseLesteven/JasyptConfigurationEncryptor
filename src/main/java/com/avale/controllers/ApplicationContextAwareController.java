package com.avale.controllers;

import javafx.scene.Node;
import javafx.stage.Window;

/**
 * Element that are {@link ApplicationContextAware} typically are controller classes needing to access to the application context.
 */
public interface ApplicationContextAwareController extends ApplicationContextAware {

	default Window getWindow() {
		return getNode().getParent().getScene().getWindow();
	}

	/**
	 * @return
	 * @implSpec return one of the GUI node reference to let this implementation work.
	 */
	Node getNode();
}
