package com.avale.model;


import com.avale.model.exception.InvalidFileException;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

	@Test
	public void applyOn_correctlyReplaceContentInText() {
		ConfigurationFile configurationFile = configurationBasedOnFile(TEST_TEXT_FILE);
		configurationFile.apply(new Replacement(0, 18, "armadillo is"));
		assertThat(configurationFile.text()).isEqualTo("armadillo is here");
	}
}