package ru.cifrak.telecomit.backend.auth.esia.error;

public class IncorrectJsonError extends EsiaError /* ValueError */ {
    public IncorrectJsonError(String message) {
        super(message);
    }

    public IncorrectJsonError(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectJsonError(Throwable cause) {
        super(cause);
    }
}
