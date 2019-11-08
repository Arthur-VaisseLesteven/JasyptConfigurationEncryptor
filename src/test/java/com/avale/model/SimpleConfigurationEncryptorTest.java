package com.avale.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleConfigurationEncryptorTest {

	@Test
	public void encrypt_correctlyEncryptTheSelection() {
		// GIVEN sample data
		Configuration configuration = TestConfiguration.Builder.testConfiguration().withLines("property.value=foo").build();
		SimpleSelection selection = new SimpleSelection("property.value=foo".indexOf("foo"), "property.value=foo".length());
		EncryptionSettings encryptionSettings = new EncryptionSettings(EncryptionSettings.availablePasswordBasedEncryptionAlgorithms().get(0), "password", 10);
		// WHEN encrypting the configuration
		new SimpleConfigurationEncryptor().encrypt(selection, configuration, encryptionSettings);
		// THEN the selection is now replaced by ENC(....)
		assertThat(configuration.text()).matches("property.value=ENC(.*)");
	}

}