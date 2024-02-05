package com.getcode.exception.common;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<BusinessExceptionResponse> handleBusinessException(BusinessException e) {
        BusinessExceptionResponse response = BusinessExceptionResponse.from(e);
        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BusinessExceptionResponse> handleValidationExceptions(ConstraintViolationException ex){
        return ResponseEntity.badRequest()
                .body(new BusinessExceptionResponse(400, ex.getMessage().substring(19)));
    }
}
