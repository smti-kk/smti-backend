package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cifrak.telecomit.backend.entities.Changes;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangesDto {

    private Integer id;

    private String name;

    private String apType;

    public ChangesDto(Changes entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.apType = entity.getApType().getName();
    }

}
