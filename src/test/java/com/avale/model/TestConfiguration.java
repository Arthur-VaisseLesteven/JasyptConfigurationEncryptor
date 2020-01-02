package com.avale.model;

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
			this.text = String.join("\n", Arrays.asList(lines));
			return this;
		}

		TestConfiguration build() {
			return new TestConfiguration(this.text, this.name, this.settings);
		}
	}

	private String text;
	private final String name;
	private EncryptionSettings settings;
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
	public void setEncryptionSettings(final EncryptionSettings settings) {
		this.settings = settings;
	}

	@Override
	public void save() {
		// nothing to do as there is no persistence layer in this type of configuration.
	}

	@Override
	protected void setText(String configuration) {
		this.text = configuration;
	}

	List<ConfigurationChange> changeHistory() {
		return Collections.unmodifiableList(changes);
	}
}
