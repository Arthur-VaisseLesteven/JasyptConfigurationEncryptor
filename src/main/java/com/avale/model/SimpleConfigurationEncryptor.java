package com.avale.model;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.salt.RandomIVGenerator;
import org.jasypt.salt.RandomSaltGenerator;

public class SimpleConfigurationEncryptor {
	/**
	 * Encrypt's the selection of the configuration using the given encryption settings.
	 */
	public void encrypt(SimpleSelection selection, Configuration configuration, EncryptionSettings encryptionSettings) {
		SimpleStringPBEConfig config = defaultEncryptionConfiguration();
		applySettings(config, encryptionSettings);

		StringEncryptor jceStringEncryptor = StringEncryptor.basedOn(standardJasyptStringEncryptor(encryptionSettings, config)::encrypt);

		String encryptedSelection = jceStringEncryptor.apply(selection.applyOn(configuration));
		Replacement replacement = new Replacement(selection.startIndex(), selection.endIndex(), encryptedSelection);
		configuration.apply(replacement);
		configuration.setEncryptionSettings(encryptionSettings);
	}

	/**
	 * Decrypt's the selection of the configuration using the given encryption settings.
	 */
	public void decrypt(SimpleSelection simpleSelection, Configuration configuration, EncryptionSettings encryptionSettings) {
		SimpleStringPBEConfig config = defaultEncryptionConfiguration();
		applySettings(config, encryptionSettings);

		StringDecryptor jceStringEncryptor = StringDecryptor.basedOn(standardJasyptStringEncryptor(encryptionSettings, config)::decrypt);

		String decryptedSelection = jceStringEncryptor.apply(simpleSelection.applyOn(configuration));
		Replacement replacement = new Replacement(simpleSelection.startIndex(), simpleSelection.endIndex(), decryptedSelection);
		configuration.apply(replacement);
	}

	private SimpleStringPBEConfig defaultEncryptionConfiguration() {
		SimpleStringPBEConfig pbeConfig = new SimpleStringPBEConfig();
		pbeConfig.setSaltGenerator(new RandomSaltGenerator());
		pbeConfig.setIvGenerator(new RandomIVGenerator());

		return pbeConfig;
	}

	private void applySettings(SimpleStringPBEConfig config, EncryptionSettings settings) {
		config.setPassword(settings.masterPassword());
		config.setKeyObtentionIterations(settings.numberOfIteration());
	}

	private StandardPBEStringEncryptor standardJasyptStringEncryptor(EncryptionSettings encryptionSettings, SimpleStringPBEConfig pbeConfig) {
		StandardPBEStringEncryptor pbeStringEncryptor = new StandardPBEStringEncryptor();
		pbeStringEncryptor.setConfig(pbeConfig);
		pbeStringEncryptor.setAlgorithm(encryptionSettings.algorithm());

		return pbeStringEncryptor;
	}
}
