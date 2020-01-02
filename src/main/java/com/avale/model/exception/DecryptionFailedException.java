package com.avale.model.exception;

public class DecryptionFailedException extends BusinessException {
	public DecryptionFailedException(Exception e) {
		super("error.decryption.failure", e);
	}
}
