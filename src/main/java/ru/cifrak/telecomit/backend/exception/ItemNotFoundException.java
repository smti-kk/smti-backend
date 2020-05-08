package ru.cifrak.telecomit.backend.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Integer id) {
        super("Item not found: " + id);
    }
    public ItemNotFoundException(Long id) {
        super("Item not found: " + id);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemNotFoundException(Throwable cause) {
        super(cause);
    }

}
