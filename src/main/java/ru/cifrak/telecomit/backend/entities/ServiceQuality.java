package ru.cifrak.telecomit.backend.entities;


/**
 * Качество услуги
 */
public enum ServiceQuality {
    GOOD,
    NORMAL,
    ABSENT;

    @Override
    public String toString() {
        switch (this) {
            case GOOD:   return "Хорошо";
            case NORMAL:  return "Удовлетворительно";
            case ABSENT:  return "Отсутствует";
            default:    throw new IllegalArgumentException();
        }
    }
}
