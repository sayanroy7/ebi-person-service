
package com.ebi.app.controller;


import com.ebi.app.model.web.response.Error;
import com.ebi.app.model.web.response.ErrorResponse;
import com.ebi.app.model.web.response.WebApiResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.MimeType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

/**
 * Top level exception handler
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<WebApiResponse<String>> handleException(Exception e) {
        return new ResponseEntity<>(WebApiResponse.error(ErrorResponse.builder()
                .message(e.getMessage()).statusCode(HttpStatus.INTERNAL_SERVER_ERROR).build()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<WebApiResponse<String>> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(WebApiResponse.error(ErrorResponse.builder()
                .message(e.getMessage()).statusCode(HttpStatus.UNAUTHORIZED).build()), HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest webRequest) {
        var webApiResponse = new WebApiResponse<>();
        var errors = ex.getBindingResult().getFieldErrors().stream().map(e -> Error.builder()
                .fieldName(e.getField())
                .defaultMessage(e.getDefaultMessage())
                .errorMessage(e.toString()).build())
                .collect(Collectors.toList());
        ex.getBindingResult().getGlobalErrors().forEach(e -> errors.add(Error.builder()
                .fieldName(e.getObjectName())
                .defaultMessage(e.getDefaultMessage())
                .errorMessage(e.toString()).build()));
        webApiResponse.setError(ErrorResponse.builder()
                .message("Binding Errors").bindingErrors(errors).statusCode(HttpStatus.BAD_REQUEST).build());
        return new ResponseEntity<>(webApiResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NonNull HttpRequestMethodNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest webRequest) {
        var webApiResponse = new WebApiResponse<>();
        webApiResponse.setError(ErrorResponse.builder()
                .statusCode(HttpStatus.METHOD_NOT_ALLOWED)
                .message("http request method not supported. request is not one of supported methods").build());
        return new ResponseEntity<>(webApiResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(@NonNull HttpMediaTypeNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest webRequest) {

        var webApiResponse = new WebApiResponse<>();
        var sb = new StringBuilder()
                .append("http media type not supported. media type is not one of supported types: ")
                .append(ex.getSupportedMediaTypes().stream()
                        .map(MimeType::getType).collect(Collectors.joining(",")));
        webApiResponse.setError(ErrorResponse.builder()
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .message(sb.toString()).build());
        return new ResponseEntity<>(webApiResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(@NonNull HttpMediaTypeNotAcceptableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest webRequest) {

        var webApiResponse = new WebApiResponse<>();
        var sb = new StringBuilder()
                .append("http media type not acceptable. media type is not one of supported types: ")
                .append(ex.getSupportedMediaTypes().stream().map(MimeType::getType).collect(Collectors.joining(",")));
        webApiResponse.setError(ErrorResponse.builder()
                .statusCode(HttpStatus.NOT_ACCEPTABLE)
                .message(sb.toString()).build());
        return new ResponseEntity<>(webApiResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        var webApiResponse = new WebApiResponse<>();
        webApiResponse.setError(ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST)
                .message("http request payload is not readable").build());
        return new ResponseEntity<>(webApiResponse, HttpStatus.BAD_REQUEST);
    }
}
