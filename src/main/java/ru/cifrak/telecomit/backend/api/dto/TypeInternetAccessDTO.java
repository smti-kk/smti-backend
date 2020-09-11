package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.TypeInternetAccess;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TypeInternetAccessDTO implements Serializable {
    private Integer id;
    private String  name;

    public TypeInternetAccessDTO(TypeInternetAccess item) {
        this.id = item.getId();
        this.name = item.getName();
    }
}
