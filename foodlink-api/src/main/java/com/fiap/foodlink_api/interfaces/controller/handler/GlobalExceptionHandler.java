package com.fiap.foodlink_api.interfaces.controller.handler;

import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.domain.exception.UserTypeAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.interfaces.controller.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserTypeNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserTypeNotFound(UserTypeNotFoundException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserTypeAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserTypeAlreadyExists(UserTypeAlreadyExistsException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
		return ResponseEntity.status(status)
				.body(new ErrorResponse(message, status.value(), Instant.now()));
	}
}
