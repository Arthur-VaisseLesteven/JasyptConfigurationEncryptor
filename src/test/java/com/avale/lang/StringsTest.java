package com.avale.lang;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;


public class StringsTest {

	@Test
	public void join_succeedOnEmptyIterable() {
		Assertions.assertThat(Strings.join(Collections.emptyList(), ",")).isEqualTo("");
	}

	@Test
	public void join_succeedOnSingleElementIterable() {
		Assertions.assertThat(Strings.join(Collections.singletonList("foo"), ",")).isEqualTo("foo");
	}

	@Test
	public void join_succeedOnMultiElementIterable() {
		Assertions.assertThat(Strings.join(Arrays.asList("foo", "bar"), ",")).isEqualTo("foo,bar");
	}

}