package com.avale.views.components;

import com.avale.controllers.AlertFactory;
import com.avale.controllers.FileTabController;
import com.avale.model.ConfigurationFile;
import com.avale.model.exception.BadEncodingException;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.Charset.*;

public class FilesTabPanel extends TabPane {

	public FilesTabPanel() {
		super();
	}

	public void open(final File file) {
		Optional<FileTab> fileTab = tabDisplaying(file);
		if (!fileTab.isPresent()) {
			fileTab = openFileTab(file);
		}

		fileTab.ifPresent(this::select);
	}

	/**
	 * Returns the tab currently displaying this file if such file exists.
	 */
	private Optional<FileTab> tabDisplaying(File file) {
		return this.getTabs().stream().map(FileTab.class::cast).filter(tab -> tab.contains(file)).findAny();
	}

	private void select(FileTab fileTab) {
		this.getSelectionModel().select(fileTab);
	}

	private Optional<FileTab> openFileTab(File file) {
		Optional<ConfigurationFile> configuration = getConfiguration(file, StandardCharsets.UTF_8);
		if (configuration.isPresent()) {
			FileTab fileTab = new FileTab(configuration.get());
			this.getTabs().add(fileTab);
			return Optional.of(fileTab);
		} else {
			return Optional.empty();
		}
	}

	private Optional<ConfigurationFile> getConfiguration(File file, Charset charset) {
		try {
			return Optional.of(new ConfigurationFile(file, charset));
		} catch (BadEncodingException badEncodingException) {
			// in case a reading error happened let the user define another encoding  in case he want to retry to load the file.
			Optional<Charset> selectedCharset = getAlertFactory().askUserInput("input.encoding.title", "input.encoding.question", availableEncodings());
			return selectedCharset.isPresent() ? getConfiguration(file, selectedCharset.get()) : Optional.empty();
		}
	}

	private Charset[] availableEncodings() {
		List<Charset> charsets = new ArrayList<>(availableCharsets().values());
		charsets.sort(Comparator.comparing(Charset::displayName));

		return charsets.toArray(new Charset[]{});
	}

	private AlertFactory getAlertFactory() {
		return new AlertFactory(this.lookup("#filesPanel").getScene().getWindow());
	}

	public Optional<FileTabController> currentTabController() {
		return currentSelectedTab().map(FileTab::controller);
	}

	private Optional<FileTab> currentSelectedTab() {
		return this.getTabs().stream().filter(Tab::isSelected).findFirst().map(FileTab.class::cast);
	}
}
