package com.avale.model.exception;

public class BusinessException extends RuntimeException {
	public BusinessException(String messageKey, Exception e) {
		super(messageKey, e);
	}

	BusinessException(String messageKey) {
		super(messageKey);
	}
}
