package com.avale.controllers;

import com.avale.model.EncryptionSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore // do not works...
public class FileTabControllerTest {

	private TextArea configurationText;
	private ChoiceBox<String> algorithm;
	private TextField encryptIteration;
	private TextField masterPassword;

	private FileTabController controller;

	@Before
	public void setup() {
		configurationText = mock(TextArea.class);
		algorithm = (ChoiceBox<String>) mock(ChoiceBox.class);
		encryptIteration = mock(TextField.class);
		masterPassword = mock(TextField.class);

		//controller = new FileTabController(configurationText, algorithm, encryptIteration, masterPassword); fully qualified constructor goes here
	}

	@Test
	public void initialize_fillInAvailableAlgorithms() {
		ObservableList<String> availableAlgorithms = FXCollections.observableArrayList();
		when(algorithm.getItems()).thenReturn(availableAlgorithms);

		controller.initialize();

		assertThat(availableAlgorithms).containsExactly(EncryptionSettings.availablePasswordBasedEncryptionAlgorithms().toArray(new String[]{}));
	}
}