package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.Operator;

@Data
public class CatalogsOperatorDTO {
    private Integer id;
    private String icon;
    private String name;

    public CatalogsOperatorDTO(Operator entity) {
        this.id = entity.getId();
        this.icon = entity.getIcon();
        this.name = entity.getName();
    }
}
