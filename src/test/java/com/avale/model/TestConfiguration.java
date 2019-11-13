package com.avale.model;

import com.avale.lang.strings.StringJoiner;

import java.util.*;

public class TestConfiguration extends BaseConfiguration {

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
			this.text = new StringJoiner("\n").join(Arrays.asList(lines));
			return this;
		}

		TestConfiguration build() {
			return new TestConfiguration(this.text, this.name, this.settings);
		}
	}

	private String text;
	private final String name;
	private final EncryptionSettings settings;
	private List<ConfigurationChange> changes = new ArrayList<>();

	private TestConfiguration(String text, String name, EncryptionSettings settings) {
		this.text = text;
		this.name = name;
		this.settings = settings;

		this.onContentReplacement(changes::add);
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
	protected void setText(String configuration) {
		this.text = configuration;
	}

	public List<ConfigurationChange> changeHistory() {
		return Collections.unmodifiableList(changes);
	}
}
