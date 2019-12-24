package com.avale.model.exception;

public class EncryptionFailedException extends BusinessException {
	public EncryptionFailedException(Exception e) {
		super("error.encryption.failure", e);
	}
}
