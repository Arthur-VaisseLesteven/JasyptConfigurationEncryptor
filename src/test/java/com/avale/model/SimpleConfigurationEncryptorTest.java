package com.avale.model;

import com.avale.model.exception.BusinessException;
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
	public void encrypt_throwEncryptionFailedExceptionWhenTheEncryptionFail() {
		// GIVEN sample data
		TestConfiguration configuration = singlePropertyConfiguration();

		// WHEN I encrypt the configuration using a broken encryptor
		assertThatCode(() -> nonWorkingEncryptor().encrypt(propertySelection(), configuration, ENCRYPTION_SETTINGS))
				// THEN I got a business exception I will be able to catch in client code
				.isInstanceOf(EncryptionFailedException.class)
				// AND  that allows to trace back the failure to its root in case of need.
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

	@Test
	public void decrypt_throwDecryptionFailedExceptionWhenDecryptionFail() {
		// GIVEN sample encrypted data
		Configuration configuration = singlePropertyConfiguration();
		configurationEncryptor.encrypt(propertySelection(), configuration, ENCRYPTION_SETTINGS);

		// WHEN I derypt the configuration using a broken encryptor
		assertThatCode(() -> nonWorkingEncryptor().decrypt(encryptedPropertySelection(configuration.text()), configuration, ENCRYPTION_SETTINGS))
				// THEN I got a business exception I will be able to catch in client code
				.isInstanceOf(BusinessException.class)
				// AND that allow to trace back the failure to its root in case of need
				.hasRootCauseExactlyInstanceOf(RuntimeException.class);
	}

	private SimpleConfigurationEncryptor nonWorkingEncryptor() {
		StringEncryptor brokenEncryptor = contentToEncrypt -> {
			throw new RuntimeException("encryption failed for whatever reason");
		};
		StringDecryptor brokenDecryptor = contentToDecrypt -> {
			throw new RuntimeException("decryption failed for whatever reason");
		};
		return new SimpleConfigurationEncryptor(encryptionSettings -> brokenEncryptor, encryptionSettings -> brokenDecryptor);
	}
}