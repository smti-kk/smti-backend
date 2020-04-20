package ru.cifrak.telecomit.backend.auth.esia.error;

public class EsiaError extends RuntimeException {
    public EsiaError(String message) {
        super(message);
    }

    public EsiaError(String message, Throwable cause) {
        super(message, cause);
    }

    public EsiaError(Throwable cause) {
        super(cause);
    }
}
