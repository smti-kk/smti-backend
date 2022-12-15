package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.Organization;

@Data
public class ReportOrganizationDTO {
    private Integer id;
    private String name;
    private TypeOrgDTO type;
    private TypeSmoDTO smo;
    private LocationDTO location;
    private String funCustomer;

    public ReportOrganizationDTO(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.type = organization.getType() != null ? new TypeOrgDTO(organization.getType()) : null;
        this.smo = organization.getSmo() != null ? new TypeSmoDTO(organization.getSmo()) : null;
        this.location = organization.getLocation() != null ? new LocationDTO(organization.getLocation()) : null;
        this.funCustomer = organization.getFunCustomer() != null ? organization.getFunCustomer().getName() : null;
    }
}
