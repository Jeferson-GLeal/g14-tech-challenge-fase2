package com.fiap.foodlink_api.interfaces.controller.handler;

import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.domain.exception.AddressNotFoundException;
import com.fiap.foodlink_api.domain.exception.MenuItemNotFoundException;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserTypeAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.interfaces.controller.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.databind.exc.MismatchedInputException;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserTypeNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserTypeNotFound(UserTypeNotFoundException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AddressNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleAddressNotFound(AddressNotFoundException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RestaurantNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleRestaurantNotFound(RestaurantNotFoundException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MenuItemNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleMenuItemNotFound(MenuItemNotFoundException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserTypeAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserTypeAlreadyExists(UserTypeAlreadyExistsException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
		return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
		String message = "Erro ao processar requisicao: valor invalido no corpo da requisicao";

		InvalidFormatException invalidFormatException = findInvalidFormatException(exception);
		if (invalidFormatException != null) {
			if (invalidFormatException.getTargetType().isEnum()) {
				String invalidValue = String.valueOf(invalidFormatException.getValue());

				String validValues = Arrays.stream(invalidFormatException.getTargetType().getEnumConstants())
						.map(Object::toString)
						.collect(Collectors.joining(", "));

				message = String.format(
						"Valor invalido '%s' para o campo '%s'. Valores aceitos: [%s]",
						invalidValue,
						resolveFieldPath(invalidFormatException),
						validValues
				);
			}
		} else {
			MismatchedInputException mismatchedInputException = findMismatchedInputException(exception);
			if (mismatchedInputException != null) {
				message = String.format(
						"Formato invalido para o campo '%s'. Tipo esperado: %s.",
						resolveFieldPath(mismatchedInputException),
						resolveExpectedType(mismatchedInputException.getTargetType())
				);
			}
		}

		return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
	}

	private InvalidFormatException findInvalidFormatException(Throwable exception) {
		Throwable current = exception;

		while (current != null) {
			if (current instanceof InvalidFormatException invalidFormatException) {
				return invalidFormatException;
			}

			current = current.getCause();
		}

		return null;
	}

	private MismatchedInputException findMismatchedInputException(Throwable exception) {
		Throwable current = exception;

		while (current != null) {
			if (current instanceof MismatchedInputException mismatchedInputException) {
				return mismatchedInputException;
			}

			current = current.getCause();
		}

		return null;
	}

	private String resolveFieldPath(JacksonException exception) {
		String fieldPath = exception.getPath().stream()
				.map(this::resolveFieldPathReference)
				.filter(reference -> !reference.isBlank())
				.collect(Collectors.joining("."));

		if (fieldPath.isBlank()) {
			return "corpo da requisicao";
		}

		return fieldPath;
	}

	private String resolveFieldPathReference(JacksonException.Reference reference) {
		if (reference.getPropertyName() != null) {
			return reference.getPropertyName();
		}

		if (reference.getIndex() >= 0) {
			return "[" + reference.getIndex() + "]";
		}

		return "";
	}

	private String resolveExpectedType(Class<?> targetType) {
		if (targetType == null) {
			return "tipo compativel com o campo";
		}

		if (targetType.isEnum()) {
			return "um dos valores aceitos do enum";
		}

		return targetType.getSimpleName();
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
		return ResponseEntity.status(status)
				.body(new ErrorResponse(message, status.value(), Instant.now()));
	}
}
