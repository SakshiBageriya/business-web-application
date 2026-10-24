package com.ganesh.drycleaning.exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f -> errors.put(f.getField(), f.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("message", "Please correct the highlighted fields.", "errors", errors));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> missing(NotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> invalid(Exception e) {
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid request. Check dates, status and field values."));
    }
}
