package com.angel.lda.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Angel on 2/8/2018.
 */

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceNotAllowed extends RuntimeException {

    String message;

    public ResourceNotAllowed(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
