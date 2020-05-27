package ru.cifrak.telecomit.backend.entities;


public enum ServiceQuality {
    GOOD,
    SATISFACTORILY,
    ABSENT;

    @Override
    public String toString() {
        switch (this) {
            case GOOD:   return "Хорошо";
            case SATISFACTORILY:  return "Удовлетворительно";
            case ABSENT:  return "Отсутствует";
            default:    throw new IllegalArgumentException();
        }
    }
}
