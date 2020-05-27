package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.TypeOrganization;

@Data
@AllArgsConstructor
public class TypeOrgDTO {
    private Integer id;
    private String name;

    public TypeOrgDTO(TypeOrganization type) {
        this.id = type.getId();
        this.name = type.getName();
    }
}
