package com.avale.model;

public interface StringEncryptorFactory {
	StringEncryptor generatesFrom(EncryptionSettings encryptionSettings);
}
