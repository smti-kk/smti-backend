package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypePost {
    UPS("UPS","ОПС"),
    POST("POST","Почтовая связь");

    private String id;
    private String name;

    @Override
    public String toString() {
        switch (this) {
            case UPS:   return "ОПС";
            case POST:  return "Почтовая связь";
            default:    throw new IllegalArgumentException();
        }
    }

}
