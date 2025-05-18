package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.application.GenericRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericRestResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

        });

        log.error("Request validation error {}}", errors);

        GenericRestResponse response = new GenericRestResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setErrorMessage("Validation errors have occurred");
        response.setInvalidFields(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
