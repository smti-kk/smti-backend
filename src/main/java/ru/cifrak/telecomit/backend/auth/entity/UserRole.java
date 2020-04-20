package ru.cifrak.telecomit.backend.auth.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole {
    ADMIN,
    OPERATOR,
    USER;

    @Override
    public String toString() {
        switch (this) {
            case ADMIN:     return "Администратор";
            case OPERATOR:  return "Оператор";
            case USER:      return "Пользователь";
            default:        throw new IllegalArgumentException();
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
