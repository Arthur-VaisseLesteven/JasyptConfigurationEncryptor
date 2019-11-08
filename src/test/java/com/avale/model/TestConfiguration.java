package com.avale.model;

import com.avale.lang.Strings;

import java.util.Arrays;
import java.util.Optional;

public class TestConfiguration implements Configuration {
	public static class Builder {
		private String text;
		private String name;
		private EncryptionSettings settings;

		static Builder testConfiguration() {
			return new Builder();
		}

		public Builder named(String name) {
			this.name = name;
			return this;
		}

		public Builder previouslyEncryptedWith(EncryptionSettings settings) {
			this.settings = settings;
			return this;
		}

		Builder withLines(String... lines) {
			this.text = Strings.join(Arrays.asList(lines), "\n");
			return this;
		}

		Configuration build() {
			return new TestConfiguration(this.text, this.name, this.settings);
		}
	}

	private String text;
	private final String name;
	private final EncryptionSettings settings;

	private TestConfiguration(String text, String name, EncryptionSettings settings) {
		this.text = text;
		this.name = name;
		this.settings = settings;
	}

	@Override
	public String text() {
		return text;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Optional<EncryptionSettings> encryptionSettings() {
		return Optional.ofNullable(settings);
	}

	@Override
	public void apply(Replacement replacement) {
		this.text = replacement.applyOn(text);
	}
}
