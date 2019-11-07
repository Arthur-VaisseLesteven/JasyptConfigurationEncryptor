package com.avale.model.encryptors;

import com.avale.model.Configuration;
import com.avale.model.EncryptionSettings;


public class SimpleEncryptor extends PasswordBasedConfigurationEncryptor<SimpleSelection> {
	public SimpleEncryptor(SimpleSelection selection) {
		super(selection);
	}

	@Override
	public void encrypt(Configuration configuration, EncryptionSettings settings) {
		System.out.println(this.getClass() + " is requested to encrypt " + configuration + " using " + settings);
			/*
		this.masterKey = HasFieldBasedEquality.requireNonNull(masterKey, "The provided master key should not be null.");
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(this.masterKey);
		config.setSaltGenerator(new RandomSaltGenerator());
		config.setIvGenerator(new RandomIVGenerator());
		config.setKeyObtentionIterations(KEY_OBTENTATION_ITERATIONS);

		Puis :

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setConfig(config);
		encryptor.setAlgorithm(ENCRYPTION_ALGORITHM);

		Puis a chaque String a chiffrer :
		remplace par "ENC(" + encryptor.encrypt(pValue) + ")"
		 */
	}
}
