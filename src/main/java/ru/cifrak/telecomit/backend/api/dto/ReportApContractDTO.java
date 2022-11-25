package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.*;

@Data
@AllArgsConstructor
public class ReportApContractDTO {
    private Integer id;
    private String address;
    private String contractor;
    private String declaredSpeed;
    private TypeInternetAccessDTO internetAccess;
    private String type;
    private ReportOrganizationDTO organization;
    private Long amount;
    private String number;


    public ReportApContractDTO(ApContract entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.contractor = null;
        this.declaredSpeed = entity.getDeclaredSpeed();
        this.internetAccess = entity.getInternetAccess() != null ? new TypeInternetAccessDTO(entity.getInternetAccess()) : null;
        this.type = "CONTRACT";
        this.organization = entity.getOrganization() != null ? new ReportOrganizationDTO(entity.getOrganization()) : null;
        this.amount = entity.getAmount();
        this.number = entity.getNumber();
    }

    public ReportApContractDTO(ru.cifrak.telecomit.backend.entities.AccessPointFull entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.contractor = entity.getContractor();
        this.declaredSpeed = entity.getDeclaredSpeed();
        this.internetAccess = entity.getInternetAccess() != null ? new TypeInternetAccessDTO(entity.getInternetAccess()) : null;
        this.type = "CONTRACT";
        this.organization = entity.getOrganization() != null ? new ReportOrganizationDTO(entity.getOrganization()) : null;
        this.amount = entity.getAmount();
        this.number = entity.getNumber();
    }
}