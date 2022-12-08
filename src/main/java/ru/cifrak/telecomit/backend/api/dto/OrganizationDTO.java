package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.Organization;

import java.util.UUID;

@Data
@AllArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)

public class OrganizationDTO {
    private Integer id;
    private String acronym;
    private String name;
    private String inn;
    private String kpp;
    private UUID fias;
    private String address;
    private Integer parent;
    private TypeOrgDTO type;
    private TypeSmoDTO smo;
    private LocationDTO location;

    private String funCustomer;

    public OrganizationDTO(Organization organization) {
        this.id = organization.getId();
        this.acronym = organization.getAcronym();
        this.name = organization.getName();
        this.inn = organization.getInn();
        this.kpp = organization.getKpp();
        this.fias = organization.getFias();
        this.address = organization.getAddress();
        this.parent = organization.getParent() != null ? organization.getParent().getId() : null;
        this.type = organization.getType() != null ? new TypeOrgDTO(organization.getType()) : null;
        this.smo = organization.getSmo() != null ? new TypeSmoDTO(organization.getSmo()) : null;
        this.location = organization.getLocation() != null ? new LocationDTO(organization.getLocation()) : null;
        this.funCustomer = organization.getFunCustomer();
    }
}
