package com.avale;

import java.net.URL;

public class ResourceLocator {
	/**
	 * @return THe URL of the located resource.
	 * @throws IllegalStateException In case the resource we were looking for was not found.
	 */
	public URL locate(final String resourceIdentifier) {
		URL resourceLocation = getClass().getClassLoader().getResource(resourceIdentifier);
		if (resourceLocation == null) throw new IllegalStateException("Failed to locate " + resourceIdentifier);
		return resourceLocation;
	}
}
