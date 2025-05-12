package com.denniseckerskorn.exceptions;

import com.denniseckerskorn.security.core.AuthenticationException;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 🔹 Custom application exceptions

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        return buildErrorResponse("Entity not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Object> handleDuplicateEntity(DuplicateEntityException ex, WebRequest request) {
        return buildErrorResponse("Duplicate entity: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException ex, WebRequest request) {
        return buildErrorResponse("Bad request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Object> handleInvalidData(InvalidDataException ex, WebRequest request) {
        return buildErrorResponse("Invalid data: " + ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthentication(AuthenticationException ex, WebRequest request) {
        return buildErrorResponse("Unauthorized: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // 🔹 Validation errors

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
        return buildErrorResponse("Missing required parameter: " + ex.getParameterName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex) {
        return buildErrorResponse("Missing path variable: " + ex.getVariableName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Object> handleMissingHeader(MissingRequestHeaderException ex) {
        return buildErrorResponse("Missing request header: " + ex.getHeaderName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<Object> handleServletBinding(ServletRequestBindingException ex) {
        return buildErrorResponse("Request binding failed: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFound(NoResourceFoundException ex, WebRequest request) {
        return buildErrorResponse("Invalid resource path or missing ID in request URL.", HttpStatus.NOT_FOUND);
    }

    // 🔹 JPA / Hibernate exceptions

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<Object> handlePropertyValueException(PropertyValueException ex) {
        return buildErrorResponse("Missing required field: " + ex.getPropertyName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        return buildErrorResponse("Database constraint violation: " + ex.getConstraintName(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Object> handlePersistenceException(PersistenceException ex) {
        return buildErrorResponse("Persistence error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return buildErrorResponse("Data integrity violation: " + ex.getMostSpecificCause().getMessage(), HttpStatus.CONFLICT);
    }

    // 🔹 Java & Spring Core exceptions

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse("Illegal argument: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(IllegalStateException ex) {
        return buildErrorResponse("Illegal state: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex) {
        return buildErrorResponse("Invalid parameter type: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidJson(HttpMessageNotReadableException ex) {
        return buildErrorResponse("Malformed JSON request body", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<Object> handleConversion(HttpMessageConversionException ex) {
        return buildErrorResponse("Error converting request body: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 🔹 HTTP-related

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildErrorResponse("HTTP method not supported: " + ex.getMethod(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 🔹 Fallback

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaught(Exception ex, WebRequest request) {
        logger.error("Unhandled exception", ex);
        return buildErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        return new ResponseEntity<>(error, status);
    }
}
