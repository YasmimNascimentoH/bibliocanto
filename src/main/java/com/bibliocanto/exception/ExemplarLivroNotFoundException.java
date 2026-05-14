package com.bibliocanto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExemplarLivroNotFoundException extends RuntimeException{
    public ExemplarLivroNotFoundException(String message) {
        super(message);
    }
}
