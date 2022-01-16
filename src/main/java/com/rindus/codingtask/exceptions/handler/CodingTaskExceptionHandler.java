package com.rindus.codingtask.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rindus.codingtask.exceptions.UserException;
import com.rindus.codingtask.utils.Constants;

@ControllerAdvice
public class CodingTaskExceptionHandler extends ResponseEntityExceptionHandler  {
	@ExceptionHandler(value = { UserException.class })
	protected ResponseEntity<String> handleConflict(UserException exception) {
		String message = exception.getMessage();
		if (exception.getStatus() == HttpStatus.NOT_FOUND) {
			message = Constants.USER_NOT_FOUND;
		}
		
		return new ResponseEntity<>(message, exception.getStatus());
	}
}
