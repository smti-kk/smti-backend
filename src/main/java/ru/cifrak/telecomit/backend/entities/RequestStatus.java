package ru.cifrak.telecomit.backend.entities;

public enum RequestStatus {
    ON_AGREEMENT,
    ACCEPTED,
    REJECTED,
    CANCELED;

    @Override
    public String toString() {
        switch (this) {
            case ON_AGREEMENT:  return "На согласовании";
            case ACCEPTED:  return "Согласовано";
            case REJECTED:  return "Отклонено";
            case CANCELED:  return "Отменено";
            default:    throw new IllegalArgumentException();
        }
    }
}
