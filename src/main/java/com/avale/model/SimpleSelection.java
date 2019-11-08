package com.avale.model;

import com.avale.model.Configuration;
import com.avale.model.Selection;

/**
 * Represent a simple single selection in a text. It defined a substring of the Selected text.
 * For example a SimpleSelection(0, 3) applied on "FooBar" selects "Foo".
 */
public class SimpleSelection implements Selection {
	private final int selectionStart;
	private final int selectionEnd;

	public SimpleSelection(final int selectionStart, final int selectionEnd) {
		this.selectionStart = selectionStart;
		this.selectionEnd = selectionEnd;
	}

	public String applyOn(Configuration configuration) {
		return configuration.text().substring(selectionStart, selectionEnd);
	}

	public int startIndex() {
		return selectionStart;
	}

	public int endIndex() {
		return selectionEnd;
	}
}
