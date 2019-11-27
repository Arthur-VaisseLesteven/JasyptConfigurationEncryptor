package com.avale.model;


import com.avale.model.exception.InvalidFileException;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigurationFileTest {

	private static final String TEST_TEXT_FILE = "simpleText.txt";
	private static final String TEST_FILE_WITH_ENCRYPTION_SETTINGS = "fileWithEncryptionSettings.properties";

	@Test
	public void constructor_failsUponFilesThatDoesNotExists() {
		assertThatThrownBy(() -> new ConfigurationFile(nonExistingFile()))
				.isInstanceOf(InvalidFileException.class)
				.hasMessage("error.file.doNotExists");
	}

	private File nonExistingFile() {
		File file = mock(File.class);
		when(file.exists()).thenReturn(Boolean.FALSE);
		return file;
	}

	@Test
	public void constructor_failsUponNonReadableFile() {
		assertThatThrownBy(() -> new ConfigurationFile(nonReadableFile()))
				.isInstanceOf(InvalidFileException.class)
				.hasMessage("error.file.nonReadable");
	}

	private File nonReadableFile() {
		File file = mock(File.class);
		when(file.exists()).thenReturn(Boolean.TRUE);
		when(file.canRead()).thenReturn(Boolean.FALSE);
		return file;
	}

	@Test
	public void constructor_failsUponNonWritableFile() {
		assertThatThrownBy(() -> new ConfigurationFile(nonWritableFile()))
				.isInstanceOf(InvalidFileException.class)
				.hasMessage("error.file.nonWritable");
	}

	private File nonWritableFile() {
		File file = mock(File.class);
		when(file.exists()).thenReturn(Boolean.TRUE);
		when(file.canRead()).thenReturn(Boolean.TRUE);
		when(file.canWrite()).thenReturn(Boolean.FALSE);
		return file;
	}

	@Test
	public void constructor_failsUponNonTextFile() {
		assertThatThrownBy(() -> configurationBasedOnFile("application_logo.png"))
				.isInstanceOf(InvalidFileException.class)
				.hasMessage("error.file.content");
	}

	private ConfigurationFile configurationBasedOnFile(final String s) {
		URL url = Objects.requireNonNull(getClass().getClassLoader().getResource(s), "Failed to find the test file.");
		return new ConfigurationFile(new File(url.getFile()));
	}

	@Test
	public void constructor_and_text_worksUponTextFile() {
		assertThat(configurationBasedOnFile(TEST_TEXT_FILE).text()).isEqualTo("there is\ntwo lines here");
	}

	@Test
	public void constructor_letEmptyEncryptionSettingsWhenNoneAreDefined() {
		assertThat(configurationBasedOnFile(TEST_TEXT_FILE).encryptionSettings()).isEmpty();
	}

	@Test
	public void constructor_retrievesExistingEncryptionSettings() {
		assertThat(configurationBasedOnFile(TEST_FILE_WITH_ENCRYPTION_SETTINGS).encryptionSettings()).contains(new EncryptionSettings("PBEWITHMD5ANDDES", null, 666));
	}

	@Test
	public void name_returnsTheFileName() {
		assertThat(configurationBasedOnFile(TEST_TEXT_FILE).name()).isEqualTo(TEST_TEXT_FILE);
	}

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder(); // as save update file content we will work on temporary files

	@Test
	public void save_correctlyFlushUpdatedConfigurationToFile() {
		ConfigurationFile configurationFile = configurationBasedOnFile(TEST_TEXT_FILE);

		runOnCloneOf(configurationFile, (configuration, fileBackingTheConfiguration) -> {
			configuration.apply(new Replacement(0, 3, "ENC(FOO)"));
			configuration.save();

			Assertions.assertThat(fileBackingTheConfiguration).hasContent(configuration.text());
		});
	}

	@Test
	public void save_correctlyCreateMetaDataFileNextToTheConfigurationFileWhenSaveMetadataFeatureIsActivated() {
		ConfigurationFile configurationFile = configurationBasedOnFile(TEST_TEXT_FILE);

		EncryptionSettings encryptionSettings = new EncryptionSettings(EncryptionSettings.availablePasswordBasedEncryptionAlgorithms().get(0), "password", 10);
		runOnCloneOf(configurationFile, (configuration, fileBackingTheConfiguration) -> {
			// GIVEN a configuration supposed to save meta data
			configuration.enable(ConfigurationFeatures.SAVE_META_DATA);
			configuration.setEncryptionSettings(encryptionSettings);

			// WHEN saving it
			configuration.save();

			// THEN encryption settings have been saved and are then retrieve upon opening of another configuration relying on the same file.
			assertThat(new ConfigurationFile(fileBackingTheConfiguration).encryptionSettings()).contains(new EncryptionSettings(encryptionSettings.algorithm(), null, encryptionSettings.numberOfIteration()));
		});
	}

	/**
	 * Runs the given {@code testingProcess} on a copy of the {@code configurationFile} backed by a temporary file.
	 * Aims at providing a testing framework for test which alters the {@link ConfigurationFile} file.
	 */
	private void runOnCloneOf(ConfigurationFile configurationFile, ConfigurationFileTestProcessRunningOnTemporaryFile testingProcess) {
		try {
			File cloneFile = temporaryFolder.newFile(configurationFile.name());
			Files.write(cloneFile.toPath(), configurationFile.text().getBytes(StandardCharsets.UTF_8));

			ConfigurationFile cloneBackedConfiguration = new ConfigurationFile(cloneFile);

			testingProcess.run(cloneBackedConfiguration, cloneFile);
		} catch (IOException ioException) {
			fail("Failed the temporary folder setup");
		}
	}

	public interface ConfigurationFileTestProcessRunningOnTemporaryFile {
		void run(ConfigurationFile subjectUnderTest, File temporaryFileBackingTheCOnfiguration);
	}
}