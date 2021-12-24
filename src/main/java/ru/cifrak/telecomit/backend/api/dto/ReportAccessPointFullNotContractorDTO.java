package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.*;

@Data
@AllArgsConstructor
public class ReportAccessPointFullNotContractorDTO extends ReportAccessPointFullAllDTO {
    private Integer id;
    private String address;
    private String contractor;
    private String declaredSpeed;
    private TypeInternetAccessDTO internetAccess;
    private String type;
    private ReportOrganizationDTO organization;


    public ReportAccessPointFullNotContractorDTO(AccessPoint entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.contractor = entity.getContractor();
        this.declaredSpeed = entity.getDeclaredSpeed();
        this.internetAccess = entity.getInternetAccess() != null ? new TypeInternetAccessDTO(entity.getInternetAccess()) : null;
        if (entity.getClass().isAssignableFrom(ApSMO.class)) {
            this.type = "SMO";
        } else if (entity.getClass().isAssignableFrom(ApESPD.class)) {
            this.type = "ESPD";
        } else if (entity.getClass().isAssignableFrom(ApZSPD.class)) {
            this.type = "ZSPD";
        } else if (entity.getClass().isAssignableFrom(ApRSMO.class)) {
            this.type = "RSMO";
        } else if (entity.getClass().isAssignableFrom(ApContract.class)) {
            this.type = "CONTRACT";
        }
        this.organization = entity.getOrganization() != null ? new ReportOrganizationDTO(entity.getOrganization()) : null;
    }

    public ReportAccessPointFullNotContractorDTO(AccessPointFull entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.contractor = entity.getContractor();
        this.declaredSpeed = entity.getDeclaredSpeed();
        this.internetAccess = entity.getInternetAccess() != null ? new TypeInternetAccessDTO(entity.getInternetAccess()) : null;
        this.type = entity.getType().getName();
        this.organization = entity.getOrganization() != null ? new ReportOrganizationDTO(entity.getOrganization()) : null;
    }

    public String CustomOutputOfOrgName() {
        return organization.getName();
    }
}
