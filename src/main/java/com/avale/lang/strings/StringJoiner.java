package com.avale.lang.strings;

public class StringJoiner {

	private final CharSequence delimiter;

	public StringJoiner(CharSequence delimiter) {
		this.delimiter = delimiter;
	}

	public String join(final Iterable<String> values) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String value : values) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(delimiter);
			}
			stringBuilder.append(value);
		}

		return stringBuilder.toString();
	}

}
