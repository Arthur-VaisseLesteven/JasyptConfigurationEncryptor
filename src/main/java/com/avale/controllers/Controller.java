package com.avale.controllers;

import java.text.MessageFormat;

/**
 * Defines a controller interface.
 * This aim at filling a gap in the current version of javafx that calls any initialize method of a controller class
 * in case one exists, without providing any way to know it purely from code.
 */
abstract class Controller {
	/**
	 * Method called after FXML fields injection. Should contains static behavior and binding of components.
	 */
	abstract void initialize();

	/**
	 * Validates the wiring to a sub UI element have been properly done, i.e. JavaFX properly injected it.
	 * <p>
	 * For this to work properly, the element must have an fx:id which matches the start of the controller field name.
	 * For example considering the following FXML :
	 * </p>
	 * <pre>
	 * &lt;Element xmlns="http://javafx.com/javafx/8.0.172-ea" xmlns:fx="http://javafx.com/fxml/1"
	 * fx:controller=ElementController>
	 * 	&lt;fx:include source="subElement.fxml" fx:id="fooElement">
	 * 		&lt;!-- the other fxml declare fx:controller=SubElementController on root node -->
	 * 	&lt;/fx:include>
	 * &lt;/Element>
	 * </pre>
	 * Then the controller class must look like this : <pre>
	 * public class  ElementController {
	 *    {@code @FXML} SubElementController fooElementController;
	 * }
	 * </pre>
	 * In case the field name don't start with the {@code fx:id}, JavaFX won't be able to inject the SubElementController properly.
	 * This method should be called for every {@link Controller} you'de expect to be injected from the {@link Controller#initialize()} method.
	 *
	 * @param controller The controller to checki wiring of
	 * @param name       The field name, for logging purpose.
	 */
	final void validatesWiringOf(Controller controller, String name) {
		if (controller == null) {
			throw new InvalidJavaFxWiringException(MessageFormat.format("Wiring to sub-node controller {0} failed.", name));
		}
	}
}
