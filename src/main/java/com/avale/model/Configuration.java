package com.avale.model;


import java.util.Optional;
import java.util.function.Consumer;

public interface Configuration extends RevertableChanges {
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

	/**
	 * Order the given configuration to update it state
	 */
	void apply(Replacement replacement);

	/**
	 * Register a listener of {@link Replacement} on the configuration content.
	 *
	 * @param applyReplacement The listener method to call upon replacement.
	 */
	void onContentReplacement(Consumer<ConfigurationChange> applyReplacement);

	/**
	 * Saves the {@link Replacement replacements} that were applied upon the configuration.
	 *
	 * @apiNote The logic applied upon call to this method, if any, depend upon the persistence layer used by the concrete implementation.
	 */
	void save();

	/**
	 * Activate an optional feature on the configuration.
	 */
	void enable(ConfigurationFeatures saveMetaData);
	/**
	 * Deactivate an optional feature on the configuration.
	 */
	void disable(ConfigurationFeatures saveMetaData);

	/**
	 * Whether the given feature is enabled.
	 */
	boolean isEnabled(ConfigurationFeatures configurationFeatures);
}
