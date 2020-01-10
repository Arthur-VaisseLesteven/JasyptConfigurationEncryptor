package com.avale.model.exception;

public class BadEncodingException extends InvalidFileException {

	public BadEncodingException(Exception e) {
		super("error.file.encoding");
	}

}
