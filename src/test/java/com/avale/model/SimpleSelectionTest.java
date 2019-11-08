package com.avale.model;

import org.junit.Test;

import static com.avale.model.TestConfiguration.Builder.testConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

public class SimpleSelectionTest {
	@Test
	public void applyOn_returnsCorrectStringSubset() {
		Configuration configuration = testConfiguration().withLines("foobar").build();
		assertThat(new SimpleSelection(0, 3).applyOn(configuration)).isEqualTo("foo");
	}
}