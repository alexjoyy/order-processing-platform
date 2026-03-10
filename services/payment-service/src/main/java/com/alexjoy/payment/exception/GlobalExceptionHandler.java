package com.alexjoy.payment.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(this::formatFieldError)
        .collect(Collectors.joining("; "));
    return build(HttpStatus.BAD_REQUEST, message, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(
      ConstraintViolationException ex,
      HttpServletRequest request
  ) {
    return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleBadBody(
      HttpMessageNotReadableException ex,
      HttpServletRequest request
  ) {
    return build(HttpStatus.BAD_REQUEST, "Malformed request body", request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgument(
      IllegalArgumentException ex,
      HttpServletRequest request
  ) {
    return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiError> handleIllegalState(
      IllegalStateException ex,
      HttpServletRequest request
  ) {
    return build(HttpStatus.CONFLICT, ex.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest request) {
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
  }

  private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest request) {
    ApiError body = new ApiError(
        OffsetDateTime.now().toString(),
        status.value(),
        status.getReasonPhrase(),
        message,
        request.getRequestURI()
    );
    return ResponseEntity.status(status).body(body);
  }

  private String formatFieldError(FieldError error) {
    return error.getField() + ": " + (error.getDefaultMessage() != null ? error.getDefaultMessage() : "invalid value");
  }
}
