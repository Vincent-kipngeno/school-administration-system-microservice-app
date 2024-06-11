package com.sas.clients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    private int statusCode;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private Object body;

}
