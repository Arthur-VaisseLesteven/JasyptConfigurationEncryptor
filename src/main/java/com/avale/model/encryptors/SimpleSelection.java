package com.avale.model.encryptors;

/**
 * Represent a simple single selection in a text. It defined a substring of the Selected text.
 * For example a SimpleSelection(0, 3) applied on "FooBar" selects "Foo".
 */
public class SimpleSelection implements Selection {
	final int selectionStart;
	final int selectionEnd;

	public SimpleSelection(final int selectionStart, final int selectionEnd) {
		this.selectionStart = selectionStart;
		this.selectionEnd = selectionEnd;
	}
}
