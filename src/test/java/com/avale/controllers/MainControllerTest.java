package com.avale.controllers;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MainControllerTest {

	private MainController controller;

	@Test
	public void initialize_failsUponFailedWiring() {
		given_a_controller_without_dependencies();
		assertThatExceptionOfType(InvalidJavaFxWiringException.class).isThrownBy(controller::initialize);
	}

	private void given_a_controller_without_dependencies() {
		controller = new MainController(null);
	}

	@Test
	public void initialize_passUponSuccessfulWiring() {
		given_a_controller_with_dependencies();
		assertThatCode(controller::initialize).doesNotThrowAnyException();
	}

	private void given_a_controller_with_dependencies() {
		controller = new MainController(new FilesTabController());
	}

}
