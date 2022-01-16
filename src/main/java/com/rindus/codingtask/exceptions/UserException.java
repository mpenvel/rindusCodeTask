package com.rindus.codingtask.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends Exception  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpStatus status;

	public UserException(String message) {
		super(message);
		this.status = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public UserException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
