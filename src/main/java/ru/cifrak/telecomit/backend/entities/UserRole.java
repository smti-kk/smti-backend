package ru.cifrak.telecomit.backend.entities;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole {
    ADMIN,
    GUEST,
    MUNICIPALITY,
    ORGANIZATION,
    OPERATOR;

    @Override
    public String toString() {
        switch (this) {
            case ADMIN:         return "Администратор";
            case GUEST:         return "Посетитель";
            case MUNICIPALITY:  return "Муниципалитет";
            case ORGANIZATION:  return "Оператор - Организации";
            case OPERATOR:      return "Оператор - Локации";
            default:    throw new IllegalArgumentException();
        }
    }

    public GrantedAuthority toAuthority() {
        final String roleName = this.name();
        return new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.format("ROLE_%s", roleName);
            }
        };
    }
}
