package ru.cifrak.telecomit.backend.auth.esia.error;

public class HttpError extends EsiaError /* requests.exceptions.HTTPError */ {
    public HttpError(String message) {
        super(message);
    }

    public HttpError(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpError(Throwable cause) {
        super(cause);
    }
}
