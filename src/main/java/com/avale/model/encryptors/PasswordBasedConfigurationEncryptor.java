package com.avale.model.encryptors;

import com.avale.model.Configuration;
import com.avale.model.EncryptionSettings;

public abstract class PasswordBasedConfigurationEncryptor<T extends Selection> {

	private final T selection;

	PasswordBasedConfigurationEncryptor(T selection) {
		this.selection = selection;
	}

	T getSelection() {
		return selection;
	}

	public abstract void encrypt(Configuration configuration, EncryptionSettings settings);

}
