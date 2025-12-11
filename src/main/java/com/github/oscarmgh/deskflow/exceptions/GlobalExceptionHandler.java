package com.github.oscarmgh.deskflow.exceptions;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.oscarmgh.deskflow.exceptions.auth.EmailExistsException;
import com.github.oscarmgh.deskflow.exceptions.auth.InactiveUserException;
import com.github.oscarmgh.deskflow.exceptions.auth.InvalidCredentialsException;
import com.github.oscarmgh.deskflow.exceptions.tickets.CategoryNotFoundException;
import com.github.oscarmgh.deskflow.exceptions.tickets.TicketNotFoundException;
import com.github.oscarmgh.deskflow.exceptions.tickets.UnauthorizedTicketAccessException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(
			InvalidCredentialsException ex,
			HttpServletRequest request) {

		return buildResponse(
				HttpStatus.UNAUTHORIZED,
				"INVALID_CREDENTIALS",
				ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleTicketNotFound(
			CategoryNotFoundException ex,
			HttpServletRequest request) {

		return buildResponse(
				HttpStatus.NOT_FOUND,
				"CATEGORY_NOT_FOUND",
				ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(TicketNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleTicketNotFound(
			TicketNotFoundException ex,
			HttpServletRequest request) {

		return buildResponse(
				HttpStatus.NOT_FOUND,
				"TICKET_NOT_FOUND",
				ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(UnauthorizedTicketAccessException.class)
	public ResponseEntity<ApiErrorResponse> handleUnauthorizedTicketAccess(
			UnauthorizedTicketAccessException ex,
			HttpServletRequest request) {

		return buildResponse(
				HttpStatus.FORBIDDEN,
				"UNAUTHORIZED_TICKET_ACCESS",
				ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(EmailExistsException.class)
	public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(
			EmailExistsException ex,
			HttpServletRequest request) {

		return buildResponse(
				HttpStatus.CONFLICT,
				"EMAIL_ALREADY_REGISTERED",
				ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(InactiveUserException.class)
	public ResponseEntity<ApiErrorResponse> handleUserInactive(
			InactiveUserException ex,
			HttpServletRequest request) {

		return buildResponse(
				HttpStatus.FORBIDDEN,
				"USER_INACTIVE",
				ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationErrors(
			MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		String message = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.findFirst()
				.map(e -> e.getField() + ": " + e.getDefaultMessage())
				.orElse("Error de validación");

		return buildResponse(
				HttpStatus.BAD_REQUEST,
				"VALIDATION_ERROR",
				message,
				request.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGeneralException(
			Exception ex,
			HttpServletRequest request) {

		return buildResponse(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"INTERNAL_SERVER_ERROR",
				ex.getMessage(),
				request.getRequestURI());
	}

	private ResponseEntity<ApiErrorResponse> buildResponse(
			HttpStatus status,
			String error,
			String message,
			String path) {

		ApiErrorResponse response = new ApiErrorResponse(
				status.value(),
				error,
				message,
				path,
				OffsetDateTime.now());

		return ResponseEntity.status(status).body(response);
	}
}
