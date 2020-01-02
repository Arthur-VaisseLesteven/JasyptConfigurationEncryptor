package com.avale.model;

public interface StringDecryptorFactory {
	StringDecryptor generatesFrom(EncryptionSettings encryptionSettings);
}
