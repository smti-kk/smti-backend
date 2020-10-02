package ru.cifrak.telecomit.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotAllowedException extends RuntimeException {
    public NotAllowedException(String s){super(s);}
}
