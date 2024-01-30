package com.getcode.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    @Builder
    @Getter
    private static class Body{
        private String message;
        private Object result;
    }

    public static ResponseEntity<?> makeResponse(String message, Object result, HttpStatus status){
        Body body = Body.builder()
                .message(message)
                .result(result)
                .build();

        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<?> makeResponse(HttpStatus status, String message) {
        return makeResponse(status, message);
    }
    public static ResponseEntity<?> makeResponse(HttpStatus status, Object result) {
        return makeResponse(status, result);
    }





}
