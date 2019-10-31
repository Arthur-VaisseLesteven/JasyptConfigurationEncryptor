package com.avale.views.components;

import org.junit.Test;

import java.util.Arrays;

public class FileTabTest {
	// TODO write some component test using testFX

	@Test
	public void consructor() {
		new FileTab(() -> Arrays.asList("first.config= foo", "second.config= bar"));
	}

}