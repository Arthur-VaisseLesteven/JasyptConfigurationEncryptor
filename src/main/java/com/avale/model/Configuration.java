package com.avale.model;


import java.util.Optional;

public interface Configuration {
	/** @return the content of the Configuration. */
	String text();

	/** @return the name of this configuration. */
	String name();

	/**
	 * If the configuration contains any encrypted value, it has {@link EncryptionSettings} bind to it that can be accessed.
	 *
	 * @return The encryption setting used to encrypt this configuration file, if any, otherwise empty.
	 */
	Optional<EncryptionSettings> encryptionSettings();
}
