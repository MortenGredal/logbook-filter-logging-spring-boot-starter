package com.mortengredal.logging.autoconfigure.config;

public class ErrorMessage {
	private String message;
	private String exception;
	private int statusCode;
	private String statusError;
	private String path;

	@SuppressWarnings("unused")
	public ErrorMessage() {
		//Empty constructor so we can serialize/deserialize it
	}

	public ErrorMessage(String message, String exception, int statusCode, String statusError, String path) {
		super();
		this.message = message;
		this.exception = exception;
		this.statusCode = statusCode;
		this.statusError = statusError;
		this.path = path;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusError() {
		return statusError;
	}

	public void setStatusError(String statusError) {
		this.statusError = statusError;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
