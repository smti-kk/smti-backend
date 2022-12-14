package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cifrak.telecomit.backend.entities.FunCustomer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FunCustomerDto {

    private Integer id;

    private String name;

    private String apType;

    public FunCustomerDto(FunCustomer funCustomer) {
        this.id = funCustomer.getId();
        this.name = funCustomer.getName();
        this.apType = funCustomer.getApType().getName();
    }

}
