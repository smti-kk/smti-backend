package ru.cifrak.telecomit.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class JPAException extends RuntimeException {
    public JPAException(String s){super(s);}
}
