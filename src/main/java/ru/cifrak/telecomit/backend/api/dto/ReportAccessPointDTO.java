package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.ApESPD;
import ru.cifrak.telecomit.backend.entities.ApSMO;

import java.io.Serializable;


@Data
@AllArgsConstructor
public class ReportAccessPointDTO implements Serializable {
    private Integer id;
    private String address;
    private String contractor;
    private String customer;
    private String declaredSpeed;
    private TypeInternetAccessDTO internetAccess;
    private String type;


    public ReportAccessPointDTO(AccessPoint entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.contractor = entity.getContractor();
        this.customer = entity.getCustomer();
        this.declaredSpeed = entity.getDeclaredSpeed();
        this.internetAccess = entity.getInternetAccess() != null? new TypeInternetAccessDTO(entity.getInternetAccess()) : null;
        if (entity.getClass().isAssignableFrom(ApSMO.class)){
            this.type = "SMO";
        } else if (entity.getClass().isAssignableFrom(ApESPD.class)){
            this.type = "ESPD";
        }
    }
}
