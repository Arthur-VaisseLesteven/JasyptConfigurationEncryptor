package com.avale.model;

import java.util.List;

public interface Configuration {
	/** Contains the content of the Configuration, line by line. */
	List<String> configurationLines();

	/** The name of this configuration. */
	String name();
}
