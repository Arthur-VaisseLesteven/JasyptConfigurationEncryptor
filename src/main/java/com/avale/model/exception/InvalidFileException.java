package com.avale.model.exception;

public class InvalidFileException extends BusinessException {
	public InvalidFileException(final String messageKey) {
		super(messageKey);
	}
}
