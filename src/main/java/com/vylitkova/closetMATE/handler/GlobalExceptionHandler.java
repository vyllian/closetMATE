package com.vylitkova.closetMATE.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<String> handleException() {
//        return ResponseEntity.notFound().build();
//    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        return ResponseEntity.badRequest().body(ex.getMessage());
//    }
}
