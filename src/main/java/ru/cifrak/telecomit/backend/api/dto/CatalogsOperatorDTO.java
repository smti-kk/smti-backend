package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.domain.CatalogsOperator;

@Data
public class CatalogsOperatorDTO {
    private Integer id;
    private String icon;
    private String name;

    public CatalogsOperatorDTO(CatalogsOperator entity) {
        this.id = entity.getId();
        this.icon = entity.getIcon();
        this.name = entity.getName();
    }
}
