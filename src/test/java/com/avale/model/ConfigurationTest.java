package com.avale.model;


import com.avale.model.exception.InvalidFileException;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigurationTest {

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
		assertThatThrownBy(() -> new ConfigurationFile(new File(getClass().getClassLoader().getResource("application_logo.png").getFile())))
				.isInstanceOf(InvalidFileException.class)
				.hasMessage("error.file.content");
	}

	@Test
	public void constructor_and_getConfigurationLines_worksUponTextFile() {
		Configuration configuration = new ConfigurationFile(new File(getClass().getClassLoader().getResource("simpleText.txt").getFile()));
		assertThat(configuration.getConfigurationLines()).containsExactly("there is", "two lines here");
	}
}