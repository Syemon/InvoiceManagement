package com.syemon.invoicemanagement.domain;

import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidDomainFieldsException extends RuntimeException {

    private Map<String, String> errors;

    public InvalidDomainFieldsException(String message) {
        super(message);
    }

    public InvalidDomainFieldsException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
