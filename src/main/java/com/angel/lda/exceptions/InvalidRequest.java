package com.angel.lda.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidRequest extends RuntimeException{
    String message;

    public InvalidRequest(String message) {
        super(message);
        this.message = message;
    }
}
