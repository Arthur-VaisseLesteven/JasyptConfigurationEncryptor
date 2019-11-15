package com.avale.model;

import java.util.Objects;

public class ConfigurationChange {
	private final Replacement replacement;
	public final String initialValue;
	public final String finalValue;

	private ConfigurationChange(Replacement replacement, String initialValue, String finalValue) {
		this.replacement = replacement;
		this.initialValue = initialValue;
		this.finalValue = finalValue;

	}

	static ConfigurationChange applyingReplacementOn(Replacement replacement, String configurationText) {
		return new ConfigurationChange(replacement, configurationText, replacement.applyOn(configurationText));
	}

	static ConfigurationChange reverting(ConfigurationChange configurationChange) {
		return new ConfigurationChange(configurationChange.replacement, configurationChange.finalValue, configurationChange.initialValue);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ConfigurationChange that = (ConfigurationChange) o;
		return Objects.equals(replacement, that.replacement) &&
				Objects.equals(initialValue, that.initialValue) &&
				Objects.equals(finalValue, that.finalValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(replacement, initialValue, finalValue);
	}

	@Override
	public String toString() {
		return "ConfigurationChange{" +
				"replacement=" + replacement +
				", initialValue='" + initialValue + '\'' +
				", finalValue='" + finalValue + '\'' +
				'}';
	}
}
