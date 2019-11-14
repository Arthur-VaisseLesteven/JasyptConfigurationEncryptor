package com.avale.model;

import java.util.function.Function;
import java.util.regex.Pattern;

public interface StringDecryptor extends Function<String, String> {

	Pattern ENCRYPTED_STRING_PATTERN = Pattern.compile("^ENC\\(.*\\)$");

	static String format(String encryptedString) {
		return ENCRYPTED_STRING_PATTERN.matcher(encryptedString).matches() ?
				encryptedString.substring(0, encryptedString.length() - 1).replace("ENC(", "") : encryptedString;
	}

	static StringDecryptor basedOn(Function<String, String> decryptionMethod) {
		return encryptedString -> decryptionMethod.apply(format(encryptedString));
	}
}
