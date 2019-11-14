package com.avale.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleConfigurationEncryptorTest {

	public static final EncryptionSettings ENCRYPTION_SETTINGS = new EncryptionSettings(EncryptionSettings.availablePasswordBasedEncryptionAlgorithms().get(0), "password", 10);
	public static final String PROPERTY_KEY = "property";
	public static final String PROPERTY = PROPERTY_KEY + "=foo";

	@Test
	public void encrypt_correctlyEncryptTheSelection() {
		// GIVEN sample data
		TestConfiguration configuration = singlePropertyConfiguration();
		// WHEN encrypting the configuration
		new SimpleConfigurationEncryptor().encrypt(propertySelection(), configuration, ENCRYPTION_SETTINGS);
		// THEN the selection is now replaced by ENC(....)
		assertThat(configuration.text()).matches(PROPERTY_KEY + "=ENC\\(.*\\)");
	}

	private SimpleSelection propertySelection() {
		return new SimpleSelection(PROPERTY.indexOf("foo"), PROPERTY.length());
	}

	private TestConfiguration singlePropertyConfiguration() {
		return TestConfiguration.Builder.testConfiguration().withLines(PROPERTY).build();
	}

	@Test
	public void decrypt_correctlyDecryptEncryptedSelection() {
		// GIVEN sample encrypted data
		SimpleConfigurationEncryptor simpleConfigurationEncryptor = new SimpleConfigurationEncryptor();
		Configuration configuration = singlePropertyConfiguration();
		simpleConfigurationEncryptor.encrypt(propertySelection(), configuration, ENCRYPTION_SETTINGS);
		// WHEN decrypting them
		simpleConfigurationEncryptor.decrypt(encryptedPropertySelection(configuration.text()), configuration, ENCRYPTION_SETTINGS);
		// THEN I got back the original content
		assertThat(configuration.text()).isEqualTo(PROPERTY);
	}

	private SimpleSelection encryptedPropertySelection(String encryptedProperty) {
		return new SimpleSelection(encryptedProperty.indexOf("ENC("), encryptedProperty.length());
	}

}