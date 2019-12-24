package com.avale.model;

import com.avale.model.exception.EncryptionFailedException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class SimpleConfigurationEncryptorTest {

	private static final EncryptionSettings ENCRYPTION_SETTINGS = new EncryptionSettings(EncryptionSettings.availablePasswordBasedEncryptionAlgorithms().get(0), "password", 10);
	private static final String PROPERTY_KEY = "property";
	private static final String PROPERTY = PROPERTY_KEY + "=foo";


	private SimpleConfigurationEncryptor configurationEncryptor = new SimpleConfigurationEncryptor();

	@Test
	public void encrypt_correctlyEncryptTheSelection() {
		// GIVEN sample data
		TestConfiguration configuration = singlePropertyConfiguration();

		// WHEN encrypting the configuration
		configurationEncryptor.encrypt(propertySelection(), configuration, ENCRYPTION_SETTINGS);

		// THEN the selection is now encrypted
		assertThat(configuration.text()).matches(PROPERTY_KEY + "=ENC\\(.*\\)");
		// AND the configuration is aware of the encryption settings used to encrypt it.
		assertThat(configuration.encryptionSettings()).contains(ENCRYPTION_SETTINGS);
	}

	@Test
	public void encrypt_throwEncryptionFailedExceptionWhenTooStrongAlgorithmIsChosen() {
		// GIVEN sample data
		TestConfiguration configuration = singlePropertyConfiguration();
		// AND encryption setting that will lead to an error during encryption.
		StringEncryptor brokenEncryptor = s -> {
			throw new RuntimeException("Encryption Failed for whatever reason");
		};
		SimpleConfigurationEncryptor configurationEncryptor = new SimpleConfigurationEncryptor(encryptionSettings -> brokenEncryptor);

		// WHEN encrypting the configuration
		assertThatCode(() -> configurationEncryptor.encrypt(propertySelection(), configuration, ENCRYPTION_SETTINGS))
				// THEN I got a business exception I will be able to catch in client code
				.isInstanceOf(EncryptionFailedException.class)
				// AND  that allows to trace back to its origin in case of need.
				.hasRootCauseExactlyInstanceOf(RuntimeException.class);
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
		Configuration configuration = singlePropertyConfiguration();
		configurationEncryptor.encrypt(propertySelection(), configuration, ENCRYPTION_SETTINGS);

		// WHEN decrypting them
		configurationEncryptor.decrypt(encryptedPropertySelection(configuration.text()), configuration, ENCRYPTION_SETTINGS);

		// THEN I got back the original content
		assertThat(configuration.text()).isEqualTo(PROPERTY);
	}

	private SimpleSelection encryptedPropertySelection(String encryptedProperty) {
		return new SimpleSelection(encryptedProperty.indexOf("ENC("), encryptedProperty.length());
	}

}