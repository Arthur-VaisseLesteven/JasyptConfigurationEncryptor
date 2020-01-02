package com.avale.model;

import org.junit.Test;

import static com.avale.model.TestConfiguration.Builder.testConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleSelectionTest {
	@Test
	public void applyOn_returnsCorrectStringSubset() {
		Configuration configuration = testConfiguration().withLines("foobar").build();
		assertThat(new SimpleSelection(0, 3).applyOn(configuration)).isEqualTo("foo");
	}

	@Test
	public void isEmpty() {
		assertTrue(new SimpleSelection(0, 0).isEmpty());
		assertTrue(new SimpleSelection(10, 10).isEmpty());
		assertFalse(new SimpleSelection(0, 10).isEmpty());
	}
}