package com.sas.clients;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseDTO<T> {

    private int statusCode;
    private String status;
    private String message;
    private Long timestamp;
    private T body;
}
