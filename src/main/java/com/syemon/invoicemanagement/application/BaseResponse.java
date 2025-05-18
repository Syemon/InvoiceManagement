package com.syemon.invoicemanagement.application;

import lombok.Data;

import java.util.Map;

@Data
public class BaseResponse<T> {
    protected int status;
    protected String errorMessage;
    protected Map<String, String> invalidFields;
    protected T data;
}
