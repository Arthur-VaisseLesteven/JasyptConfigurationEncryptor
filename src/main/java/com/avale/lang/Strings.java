package com.avale.lang;

public class Strings {
	/**
	 * @param values    The strings to join.
	 * @param delimiter The character used to delimit the different values.
	 * @return A single string built from the provided {@code values} joining them using the provided {@code delimiter}.
	 */
	public static String join(final Iterable<String> values, String delimiter) {
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
