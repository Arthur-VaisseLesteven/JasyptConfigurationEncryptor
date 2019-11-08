package com.avale.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReplacementTest {

	@Test
	public void applyOn_replacesSelectionOnString() {
		assertThat(new Replacement(0, 3, "bar").applyOn("foobare")).isEqualTo("barbare");
	}

}