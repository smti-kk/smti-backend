package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.TypeInternetAccess;

@Data
@AllArgsConstructor
public class TypeInternetAccessDTO {
    private Integer id;
    private String  name;

    public TypeInternetAccessDTO(TypeInternetAccess item) {
        this.id = item.getId();
        this.name = item.getName();
    }
}
