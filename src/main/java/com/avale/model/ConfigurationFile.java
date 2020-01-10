package com.avale.model;

import com.avale.model.exception.BadEncodingException;
import com.avale.model.exception.BusinessException;
import com.avale.model.exception.InvalidFileException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class ConfigurationFile extends BaseConfiguration {

	/** The file holding the configuration */
	private final File   file;
	/**
	 * The charset used to read/write the file
	 */
	private final Charset charset;
	/** The configuration file lines. */
	private       String configurationText;
	/**
	 * The encryption settings used to encrypt configurations inside this file.
	 */
	private EncryptionSettings encryptionSettings;

	public ConfigurationFile(final File file) {
		this(file, StandardCharsets.UTF_8);
	}

	public ConfigurationFile(final File file, Charset charset) {
		validatesInput(file);
		this.file = file;
		this.charset = Objects.requireNonNull(charset);
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
			return String.join("\n", Files.readAllLines(file.toPath(), this.charset));
		} catch (MalformedInputException exception) {
			throw new BadEncodingException(exception); // TODO add test for this behavior. Add catch block asking the user how to reload the file.
		} catch (IOException e) {
			// this point is reached when opening non text file that can't be read line by line.
			throw new InvalidFileException("error.file.content");
		}
	}

	private EncryptionSettings loadEncryptionSettings() {
		try {
			return findEncryptionSettingFile().map(this::loadEncryptionSettings).orElse(null);
		} catch (IOException e) {
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
	public void setEncryptionSettings(EncryptionSettings settings) {
		encryptionSettings = settings;
	}

	@Override
	protected void setText(String configurationText) {
		this.configurationText = configurationText;
	}

	public boolean isTiedTo(File file) {
		return this.file.equals(file);
	}

	@Override
	public String toString() {
		return "ConfigurationFile{" + this.name() + "}";
	}

	@Override
	public void save() {
		try {
			Path tempFile = Files.createTempFile(null, ".tmp");
			Files.write(tempFile, this.text().getBytes(StandardCharsets.UTF_8));
			Files.move(tempFile, this.file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException ioException) {
			throw new BusinessException("error.configuration.save", ioException);
		}

		if (isEnabled(ConfigurationFeatures.SAVE_META_DATA)) {
			try (FileOutputStream outputStream = new FileOutputStream(findEncryptionSettingFile().orElse(Paths.get(this.file.getParent() + File.separator + this.encryptionSettingsFileName())).toFile())) {
				encryptionSettings.toProperties().store(outputStream, null);
			} catch (IOException exception) {
				System.out.println("Failed to save encryption metadata.");
				exception.printStackTrace();
			}
		}
	}
}
