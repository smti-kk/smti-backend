package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.TypeSmo;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TypeSmoDTO implements Serializable {
    private Integer id;
    private String name;

    public TypeSmoDTO(TypeSmo smo) {
        this.id = smo.getId();
        this.name = smo.getName();
    }
}
