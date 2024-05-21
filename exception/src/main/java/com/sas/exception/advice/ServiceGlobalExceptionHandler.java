package com.sas.exception.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sas.exception.dto.CustomErrorResponse;
import com.sas.exception.dto.GlobalErrorCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ServiceGlobalExceptionHandler {
    @Autowired
    public GlobalErrorCode globalErrorCode;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorCode(globalErrorCode.GENERIC_ERROR)
                .errorMessage(exception.getMessage())
                .build();
        log.error("ServiceGlobalExceptionHandler::handleGenericException exception caught {}", exception.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception) throws JsonProcessingException {
        CustomErrorResponse errorResponse = new ObjectMapper().readValue(exception.getMessage(), CustomErrorResponse.class);
        log.error("ServiceGlobalExceptionHandler::handleRuntimeException exception caught {}", exception.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
