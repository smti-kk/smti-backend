package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.TypeSmo;

@Data
@AllArgsConstructor
public class TypeSmoDTO {
    private Integer id;
    private String name;

    public TypeSmoDTO(TypeSmo smo) {
        this.id = smo.getId();
        this.name = smo.getName();
    }
}
