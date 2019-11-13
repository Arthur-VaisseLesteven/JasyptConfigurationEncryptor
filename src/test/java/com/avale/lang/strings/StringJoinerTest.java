package com.avale.lang.strings;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class StringJoinerTest {

	@Test
	public void join_succeedOnEmptyIterable() {
		assertThat(new StringJoiner(",").join(Collections.emptyList())).isEqualTo("");
	}

	@Test
	public void join_succeedOnSingleElementIterable() {
		assertThat(new StringJoiner(",").join(Collections.singletonList("foo"))).isEqualTo("foo");
	}

	@Test
	public void join_succeedOnMultiElementIterable() {
		assertThat(new StringJoiner(",").join(Arrays.asList("foo", "bar"))).isEqualTo("foo,bar");
	}

}