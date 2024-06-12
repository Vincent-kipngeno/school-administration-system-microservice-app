package com.sas.clients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ResponseDTO<T> {

    private int statusCode;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private T body;
}
