package com.test.bank.error.handler;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorDTO> handleAllExceptions(Exception ex, WebRequest request) {
		ErrorDTO errorDetails = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal error",
				ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public final ResponseEntity<ErrorDTO> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex, WebRequest request) {
		ErrorDTO errorDetails = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Argument type mismatch", ex.getMessage(),
				request.getDescription(false));
		logger.error(errorDetails.getPath() + " responded with stack trace " + ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@Override
	public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDTO errorDetails = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Invalid argument", ex.getMessage(),
				request.getDescription(false));
		logger.error(errorDetails.getPath() + " responded with stack trace " + ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException ex,
			WebRequest request) {
		ErrorDTO errorDetails = new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Entity not found", ex.getMessage(),
				request.getDescription(false));
		logger.error(errorDetails.getPath() + " responded with stack trace " + ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
}
