package ru.cifrak.telecomit.backend.entities;

public enum ImportanceProblemStatus {
    LOW,
    MIDDLE,
    HIGH;

    @Override
    public String toString() {
        switch (this) {
            case LOW:  return "Низкая";
            case MIDDLE:  return "Средняя";
            case HIGH:  return "Высокая";
            default:    throw new IllegalArgumentException();
        }
    }
}
