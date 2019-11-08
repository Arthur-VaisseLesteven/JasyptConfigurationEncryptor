package com.avale.model;

import java.util.function.Function;

interface StringEncryptor extends Function<String, String> {
	static String format(String encryptedString) {
		return "ENC(" + encryptedString + ")";
	}

	static StringEncryptor basedOn(Function<String, String> encryptionMethod) {
		return s -> format(encryptionMethod.apply(s));
	}
}
