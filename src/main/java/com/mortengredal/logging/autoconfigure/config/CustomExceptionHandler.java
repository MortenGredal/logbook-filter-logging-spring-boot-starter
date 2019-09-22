package com.mortengredal.logging.autoconfigure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<ErrorMessage> handleValidationFailure(ConstraintViolationException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		StringBuilder str = new StringBuilder();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			str.append(violation.getMessage() + "\n");
		}

		ErrorMessage erroMsg = new ErrorMessage(str.toString(), ex.getClass().getName(), status.value(), status.name(), request.getRequestURI());

		return new ResponseEntity<>(erroMsg, status);
	}

	@ExceptionHandler(value = { IllegalArgumentException.class })
	protected ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		ErrorMessage erroMsg = new ErrorMessage(ex.getMessage(), IllegalArgumentException.class.getName(), status.value(), status.name(), request.getRequestURI());

		return new ResponseEntity<>(erroMsg, status);
	}

	@ExceptionHandler(value = { IllegalStateException.class })
	protected ResponseEntity<ErrorMessage> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;

		ErrorMessage erroMsg = new ErrorMessage(ex.getMessage(), IllegalStateException.class.getName(), status.value(), status.name(), request.getRequestURI());

		return new ResponseEntity<>(erroMsg, status);
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<ErrorMessage> handleErrorMessage(Exception ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		ErrorMessage erroMsg = new ErrorMessage(ex.getMessage(), ex.getClass().getName(), status.value(), status.name(), request.getRequestURI());

		return new ResponseEntity<>(erroMsg, status);
	}

}
