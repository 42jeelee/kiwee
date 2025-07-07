package kr.co.jeelee.kiwee.global.exception.handler;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import kr.co.jeelee.kiwee.global.dto.response.ErrorResponse;
import kr.co.jeelee.kiwee.global.dto.response.GlobalResponse;
import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { CustomException.class })
	protected ResponseEntity<Object> handleCustomException(CustomException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(),  ex.getDetails());
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(
					FieldError::getField,
					err -> Optional.ofNullable(err.getDefaultMessage()).orElse("Invalid value"),
					(existing, replacement) -> existing
				));

		ErrorCode errorCode = ErrorCode.REQUEST_INVALID;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), errors);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, String> errors = ex.getConstraintViolations().stream()
			.collect(Collectors.toMap(
				violation -> StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
					.reduce((first, second) -> second)
					.map(Path.Node::getName)
					.orElse("unknown"),
				ConstraintViolation::getMessage
			));

		ErrorCode errorCode = ErrorCode.REQUEST_INVALID;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), errors);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
		HttpMessageNotReadableException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		ErrorCode errorCode = ErrorCode.REQUEST_INVALID;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), Map.of("json", "읽을 수 없는 데이터입니다."));
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);
		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
		MissingServletRequestParameterException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		ErrorCode errorCode = ErrorCode.MISSING_PARAMS;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), null);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@ExceptionHandler(value = { AuthenticationException.class })
	protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), null);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@ExceptionHandler(value = { AccessDeniedException.class})
	protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
		ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), null);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
		NoHandlerFoundException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		ErrorCode errorCode = ErrorCode.NOT_FOUND;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), null);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
		HttpRequestMethodNotSupportedException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), null);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
		HttpMediaTypeNotSupportedException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		ErrorCode errorCode = ErrorCode.NOT_SUPPORT_MEDIA_TYPE;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), null);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@ExceptionHandler(value = { DataIntegrityViolationException.class })
	protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		ErrorCode errorCode = ErrorCode.REQUEST_INVALID;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), Map.of("field", "중복될 수 없는 값이 포함되어 있습니다."));
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
		Exception ex,
		Object body,
		HttpHeaders headers,
		HttpStatusCode statusCode,
		WebRequest request
	) {
		ErrorCode errorCode = ErrorCode.UNKNOWN;
		ErrorResponse errorResponse = ErrorResponse.of(
			errorCode.getCode(),
			errorCode.getMessage(),
			Map.of("message", ex.getMessage())
		);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<Object> handleUnknownException(Exception ex) {
		ErrorCode errorCode = ErrorCode.UNKNOWN;
		ErrorResponse errorResponse = ErrorResponse.of(
			errorCode.getCode(),
			errorCode.getMessage(),
			Map.of("message", ex.getMessage())
		);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}
}
