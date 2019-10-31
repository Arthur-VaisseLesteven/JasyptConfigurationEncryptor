package com.avale.model.exception;

public class BusinessException extends RuntimeException {
	public BusinessException(String messageKey) {
		super(messageKey);
	}
}
