package com.avale.model;


import com.avale.model.exception.EncryptionFailedException;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.salt.RandomIVGenerator;
import org.jasypt.salt.RandomSaltGenerator;

public class SimpleConfigurationEncryptor {

	private final StringEncryptorFactory stringEncryptorFactory;

	public SimpleConfigurationEncryptor() {
		this(encryptionSettings -> StringEncryptor.basedOn(standardJasyptStringEncryptor(encryptionSettings)::encrypt));
	}

	SimpleConfigurationEncryptor(StringEncryptorFactory encryptorFactory) {
		this.stringEncryptorFactory = encryptorFactory;
	}

	/**
	 * Encrypt's the selection of the configuration using the given encryption settings.
	 */
	public void encrypt(SimpleSelection selection, Configuration configuration, EncryptionSettings encryptionSettings) {
		StringEncryptor jceStringEncryptor = stringEncryptorFactory.generatesFrom(encryptionSettings);

		String encryptedSelection;
		try {
			encryptedSelection = jceStringEncryptor.apply(selection.applyOn(configuration));
		} catch (RuntimeException exception) {
			throw new EncryptionFailedException(exception);
		}
		Replacement replacement = new Replacement(selection.startIndex(), selection.endIndex(), encryptedSelection);
		configuration.apply(replacement);
		configuration.setEncryptionSettings(encryptionSettings);
	}

	/**
	 * Decrypt's the selection of the configuration using the given encryption settings.
	 */
	public void decrypt(SimpleSelection simpleSelection, Configuration configuration, EncryptionSettings encryptionSettings) {
		StringDecryptor jceStringEncryptor = StringDecryptor.basedOn(standardJasyptStringEncryptor(encryptionSettings)::decrypt);

		String decryptedSelection = jceStringEncryptor.apply(simpleSelection.applyOn(configuration));
		Replacement replacement = new Replacement(simpleSelection.startIndex(), simpleSelection.endIndex(), decryptedSelection);
		configuration.apply(replacement);
	}

	private static SimpleStringPBEConfig defaultEncryptionConfiguration(EncryptionSettings settings) {
		SimpleStringPBEConfig pbeConfig = new SimpleStringPBEConfig();
		pbeConfig.setSaltGenerator(new RandomSaltGenerator());
		pbeConfig.setIvGenerator(new RandomIVGenerator());

		pbeConfig.setPassword(settings.masterPassword());
		pbeConfig.setKeyObtentionIterations(settings.numberOfIteration());

		return pbeConfig;
	}

	private static StandardPBEStringEncryptor standardJasyptStringEncryptor(EncryptionSettings encryptionSettings) {
		SimpleStringPBEConfig config = defaultEncryptionConfiguration(encryptionSettings);

		StandardPBEStringEncryptor pbeStringEncryptor = new StandardPBEStringEncryptor();
		pbeStringEncryptor.setConfig(config);
		pbeStringEncryptor.setAlgorithm(encryptionSettings.algorithm());

		return pbeStringEncryptor;
	}
}
