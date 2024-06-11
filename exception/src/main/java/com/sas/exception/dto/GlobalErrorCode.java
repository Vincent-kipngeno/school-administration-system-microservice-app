package com.sas.exception.dto;

import org.springframework.beans.factory.annotation.Value;

public class GlobalErrorCode {

    @Value("${error.code.email_already_exists}")
    public String EMAIL_ALREADY_EXISTS;

    @Value("${error.code.restaurant_general}")
    public String GENERIC_ERROR;
}
