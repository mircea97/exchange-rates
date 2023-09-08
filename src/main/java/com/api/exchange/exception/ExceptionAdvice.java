package com.api.exchange.exception;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = {ResponseStatusException.class})
    public ResponseEntity<ErrorResponse> responseStatusExceptionHandler(ResponseStatusException e) {
        log.error("Unhandled exception encountered", e);
        return ResponseEntity
                .status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .code(String.valueOf(e.getStatusCode().value()))
                        .message(e.getReason())
                        .build());
    }

    @Value
    @Builder
    public static class ErrorResponse {

        @NotNull
        String code;

        @NotNull
        String message;
    }
}
