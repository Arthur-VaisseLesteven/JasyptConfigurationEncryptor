package com.avale.controllers;

/**
 * Error to throw when the application failed to get an {@link javafx.fxml.FXML @FXML} annotated field injected.
 */
class InvalidJavaFxWiringException extends RuntimeException {
	InvalidJavaFxWiringException(final String error) {
		super(error);
	}
}
