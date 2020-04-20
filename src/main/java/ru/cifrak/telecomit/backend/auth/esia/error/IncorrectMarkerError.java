package ru.cifrak.telecomit.backend.auth.esia.error;

public class IncorrectMarkerError extends EsiaError /* jwt.InvalidTokenError */ {
    public IncorrectMarkerError(String message) {
        super(message);
    }

    public IncorrectMarkerError(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectMarkerError(Throwable cause) {
        super(cause);
    }
}
