package com.avale.model;

import java.util.Objects;

/**
 * Defines a string replacement among a String
 */
public class Replacement {
	private final int fragmentToReplaceStart;
	private final int fragmentToReplaceEnd;
	private final String replacement;

	Replacement(int fragmentToReplaceStart, int fragmentToReplaceEnd, String replacement) {
		this.fragmentToReplaceStart = fragmentToReplaceStart;
		this.fragmentToReplaceEnd = fragmentToReplaceEnd;
		this.replacement = replacement;
	}

	String applyOn(final String content) {
		return content.substring(0, fragmentToReplaceStart) + replacement + content.substring(fragmentToReplaceEnd);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Replacement that = (Replacement) o;
		return fragmentToReplaceStart == that.fragmentToReplaceStart &&
				fragmentToReplaceEnd == that.fragmentToReplaceEnd &&
				Objects.equals(replacement, that.replacement);
	}

	@Override
	public int hashCode() {

		return Objects.hash(fragmentToReplaceStart, fragmentToReplaceEnd, replacement);
	}

	@Override
	public String toString() {
		return "Replacement{" +
				"fragmentToReplaceStart=" + fragmentToReplaceStart +
				", fragmentToReplaceEnd=" + fragmentToReplaceEnd +
				", replacement='" + replacement + '\'' +
				'}';
	}
}
