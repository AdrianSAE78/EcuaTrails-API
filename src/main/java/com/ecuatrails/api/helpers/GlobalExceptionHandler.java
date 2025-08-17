package com.ecuatrails.api.helpers;

import com.ecuatrails.api.dto.ApiError;
import com.ecuatrails.api.dto.ApiValidationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// ---------- 400 Bad Request ----------
	@ExceptionHandler({ IllegalArgumentException.class, HttpMessageNotReadableException.class // JSON malformado / tipos
																								// incompatibles
	})
	public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST, ex, req, null);
	}

	// Bean Validation @RequestBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpServletRequest req) {
		List<ApiValidationError> details = ex.getBindingResult().getFieldErrors().stream()
				.map(fe -> new ApiValidationError(fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue()))
				.toList();
		return build(HttpStatus.BAD_REQUEST, ex, req, details);
	}

	// Validation @RequestParam / @PathVariable
	@ExceptionHandler({ ConstraintViolationException.class, BindException.class })
	public ResponseEntity<ApiError> handleConstraintViolation(Exception ex, HttpServletRequest req) {
		List<ApiValidationError> details = new ArrayList<>();
		if (ex instanceof ConstraintViolationException cve) {
			for (ConstraintViolation<?> v : cve.getConstraintViolations()) {
				String field = v.getPropertyPath() == null ? null : v.getPropertyPath().toString();
				details.add(new ApiValidationError(field, v.getMessage(), v.getInvalidValue()));
			}
		} else if (ex instanceof BindException be) {
			be.getBindingResult().getFieldErrors().forEach(fe -> details
					.add(new ApiValidationError(fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue())));
		}
		return build(HttpStatus.BAD_REQUEST, ex, req, details);
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class })
	public ResponseEntity<ApiError> handleRequestParamIssues(Exception ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST, ex, req, null);
	}

	// ---------- 401 Unauthorized ----------
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleAuthentication(AuthenticationException ex, HttpServletRequest req) {
		return build(HttpStatus.UNAUTHORIZED, ex, req, null);
	}

	// ---------- 403 Forbidden ----------
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
		return build(HttpStatus.FORBIDDEN, ex, req, null);
	}

	// ---------- 404 Not Found ----------
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex, HttpServletRequest req) {
		return build(HttpStatus.NOT_FOUND, ex, req, null);
	}

	// ---------- 405 Method Not Allowed ----------
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest req) {
		return build(HttpStatus.METHOD_NOT_ALLOWED, ex, req, null);
	}

	// ---------- 406 Not Acceptable ----------
	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	public ResponseEntity<ApiError> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpServletRequest req) {
		return build(HttpStatus.NOT_ACCEPTABLE, ex, req, null);
	}

	// ---------- 409 Conflict ----------
	@ExceptionHandler({ IllegalStateException.class,
			DataIntegrityViolationException.class
	})
	public ResponseEntity<ApiError> handleConflict(Exception ex, HttpServletRequest req) {
		return build(HttpStatus.CONFLICT, ex, req, null);
	}

	// ---------- 413 Payload Too Large ----------
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiError> handleMaxUpload(MaxUploadSizeExceededException ex, HttpServletRequest req) {
		return build(HttpStatus.PAYLOAD_TOO_LARGE, ex, req, null);
	}

	// ---------- 415 Unsupported Media Type ----------
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, MultipartException.class })
	public ResponseEntity<ApiError> handleUnsupportedMedia(Exception ex, HttpServletRequest req) {
		return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex, req, null);
	}

	// ---------- 422 Unprocessable Entity ----------
	@ExceptionHandler(ErrorResponseException.class)
	public ResponseEntity<ApiError> handleErrorResponse(ErrorResponseException ex, HttpServletRequest req) {
		HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
		return build(status.is4xxClientError() ? status : HttpStatus.UNPROCESSABLE_ENTITY, ex, req, null);
	}

	// ---------- 500 Internal Server Error ----------
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest req) {
		log.error("Unexpected error", ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, ex, req, null);
	}

	// ---------- Helper ----------
	private ResponseEntity<ApiError> build(HttpStatus status, Exception ex, HttpServletRequest req,
			List<ApiValidationError> details) {
		String message = ex.getMessage() == null ? status.getReasonPhrase() : ex.getMessage();
		ApiError body = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message,
				req.getRequestURI(), null,
				details);
		return ResponseEntity.status(status).body(body);
	}
}