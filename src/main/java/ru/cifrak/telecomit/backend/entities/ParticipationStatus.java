package ru.cifrak.telecomit.backend.entities;

import org.springframework.security.core.GrantedAuthority;

/**
 * Статус участия в государственной программе
 */
public enum ParticipationStatus {
    DONE,
    NONE,
    PLAN;

    @Override
    public String toString() {
        switch (this) {
            case DONE:  return "Исполненно";
            case NONE:  return "Не исполненно";
            case PLAN:  return "План";
            default:    throw new IllegalArgumentException();
        }
    }

}
