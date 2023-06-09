package ru.cifrak.telecomit.backend.entities;

public enum APConnectionState {
    ACTIVE,
    DISABLED,
    PROBLEM,
    NOT_MONITORED;

    @Override
    public String toString() {
        switch (this) {
            case ACTIVE:  return "Работает";
            case DISABLED:  return "Не работает";
            case PROBLEM:  return "Проблема";
            case NOT_MONITORED:  return "Не подключено";
            default:    throw new IllegalArgumentException();
        }
    }
}
