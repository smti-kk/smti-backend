package ru.cifrak.telecomit.backend.entities;


public enum TypePost {
    UPS,
    POST;

    @Override
    public String toString() {
        switch (this) {
            case UPS:   return "УПС";
            case POST:  return "Почтовая связь";
            default:    throw new IllegalArgumentException();
        }
    }

}
