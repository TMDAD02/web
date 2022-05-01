package com.chatapp.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControladorExcepciones {

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity handleMultipartException(MultipartException ex) {
        Map<String, String> result = new HashMap<>();
        result.put("message", "Error ==> Fichero muy grande ");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(result);
    }
}
