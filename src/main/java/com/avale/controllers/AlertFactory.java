package com.avale.controllers;

import com.avale.model.exception.BusinessException;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;

import java.io.PrintWriter;
import java.io.StringWriter;


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
		alert.getDialogPane().setExpandableContent(getStacktraceDisplay(exception));

		alert.show();
	}

	private GridPane getStacktraceDisplay(BusinessException exception) {
		StringWriter writer = new StringWriter();
		exception.printStackTrace(new PrintWriter(writer));

		TextArea exceptionStacktraceDisplay = new TextArea(writer.toString());
		exceptionStacktraceDisplay.setMaxHeight(Double.MAX_VALUE);
		exceptionStacktraceDisplay.setMaxWidth(Double.MAX_VALUE);
		GridPane.setVgrow(exceptionStacktraceDisplay, Priority.ALWAYS);
		GridPane.setHgrow(exceptionStacktraceDisplay, Priority.ALWAYS);

		GridPane displayWrapper = new GridPane();
		displayWrapper.setMaxWidth(Double.MAX_VALUE);
		displayWrapper.add(exceptionStacktraceDisplay, 0, 0);
		return displayWrapper;
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
