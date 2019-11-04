package com.avale.model;

import com.avale.lang.Strings;
import com.avale.model.exception.InvalidFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigurationFile implements Configuration {

	/** The file holding the configuration */
	private final File   file;
	/** The configuration file lines. */
	private       String configurationText;

	public ConfigurationFile(final File file) {
		validatesInput(file);
		this.file = file;
		configurationText = loadConfigurationContent(file);
	}

	private void validatesInput(final File file) {
		if (!file.exists()) throw new InvalidFileException("error.file.doNotExists");
		if (!file.canRead()) throw new InvalidFileException("error.file.nonReadable");
		if (!file.canWrite()) throw new InvalidFileException("error.file.nonWritable");
	}

	private String loadConfigurationContent(final File file) {
		try {
			return Strings.join(Files.readAllLines(file.toPath()), "\n");
		} catch (IOException e) {
			// this point is reached when opening non text file that can't be read line by line.
			throw new InvalidFileException("error.file.content");
		}
	}

	@Override
	public String name() {
		return file.getName();
	}

	@Override
	public String text() {
		return configurationText;
	}

	/*
	this.masterKey = Objects.requireNonNull(masterKey, "The provided master key should not be null.");
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
