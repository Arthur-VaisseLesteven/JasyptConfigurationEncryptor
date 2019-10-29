package com.avale.controllers;

/**
 * Defines a controller interface.
 * This aim at filling a gap in the current version of javafx that calls any initialize method of a controller class
 * in case one exists, without providing any way to know it purely from code.
 */
public interface Controller {
	/**
	 * Method called after FXML fields injection. Should contains static behavior and binding of components.
	 */
	void initialize();
}
