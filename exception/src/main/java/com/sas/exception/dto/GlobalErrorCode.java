package com.sas.exception.dto;

import org.springframework.beans.factory.annotation.Value;

public class GlobalErrorCode {

    @Value("${error.code.order_not_found}")
    public String ERROR_ORDER_NOT_FOUND;

    @Value("${error.code.restaurant_general}")
    public String GENERIC_ERROR;
}
