package com.avale.controllers;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ControllerTest {

	private DummyController controller = new DummyController();

	@Test
	public void validatesWiringOf_ThrowOnNullValue() {
		assertThatExceptionOfType(InvalidJavaFxWiringException.class)
				.isThrownBy(() -> controller.validatesWiringOf(null, "nullSubController"))
				.withMessage("Wiring to sub-node controller nullSubController failed.");
	}

	@Test
	public void validatesWiringOf_PassOnNonNullValue() {
		assertThatCode(() -> controller.validatesWiringOf(new DummyController(), "nonNullSubContrller"))
				.doesNotThrowAnyException();
	}

	private static class DummyController extends Controller {
		@Override
		void initialize() {
			// nothing to do, this is a dummy implementation
		}
	}
}