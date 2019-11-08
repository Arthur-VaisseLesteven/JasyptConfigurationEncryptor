package com.avale.model;

/**
 * Defines a string replacement among a String
 */
class Replacement {
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
}
