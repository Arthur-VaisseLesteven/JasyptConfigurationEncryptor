package com.avale.model;

import com.avale.lang.Strings;
import com.avale.model.exception.InvalidFileException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public class ConfigurationFile implements Configuration {

	/** The file holding the configuration */
	private final File   file;
	/** The configuration file lines. */
	private       String configurationText;
	/**
	 * The encryption settings used to encrypt configurations inside this file.
	 */
	private EncryptionSettings encryptionSettings;

	public ConfigurationFile(final File file) {
		validatesInput(file);
		this.file = file;
		configurationText = loadConfigurationContent(file);
		encryptionSettings = loadEncryptionSettings();
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

	private EncryptionSettings loadEncryptionSettings() {
		try {
			return findEncryptionSettingFile().map(this::loadEncryptionSettings).orElse(null);
		} catch (IOException e) { // TODO probably not the good way to handle this... but can't figure out what to do for now.
			System.out.println("Encountered an exception while trying to load EncryptionSettings of " + file.getAbsolutePath() + " : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private Optional<Path> findEncryptionSettingFile() throws IOException {
		return Files.list(file.toPath().getParent()).filter(this::isEncryptionSettingsPath).findFirst();
	}

	private EncryptionSettings loadEncryptionSettings(Path encryptionSettingsPath) {
		try (InputStream settingsStream = new FileInputStream(encryptionSettingsPath.toFile())) {
			Properties properties = new Properties();
			properties.load(settingsStream);
			return new EncryptionSettings(properties);
		} catch (IOException e) {
			System.out.println("Failed encryption loading from " + encryptionSettingsPath + " due to " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private boolean isEncryptionSettingsPath(Path path) {
		return path.getFileName().toString().equals(encryptionSettingsFileName());
	}

	private String encryptionSettingsFileName() {
		return "." + file.getName() + ".jcvd";
	}

	@Override
	public String name() {
		return file.getName();
	}

	@Override
	public String text() {
		return configurationText;
	}

	@Override
	public Optional<EncryptionSettings> encryptionSettings() {
		return Optional.ofNullable(encryptionSettings);
	}

	@Override
	public void apply(Replacement replacement) {
		configurationText = replacement.applyOn(configurationText);
	}

	public boolean isTiedTo(File file) {
		return this.file.equals(file);
	}

	@Override
	public String toString() {
		return "ConfigurationFile{" + this.name() + "}";
	}
}
