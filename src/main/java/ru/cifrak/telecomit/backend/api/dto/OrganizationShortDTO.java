package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cifrak.telecomit.backend.entities.Organization;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)

public class OrganizationShortDTO {
    private Integer id;
    private String acronym;
    private String name;
    private String inn;
    private String kpp;
    private UUID fias;
    private String address;
    private Integer parent;
    private Integer type;
    private Integer smo;
    private Integer location;

    private String funCustomer;

    public OrganizationShortDTO(Organization organization) {
        this.id = organization.getId();
        this.acronym = organization.getAcronym();
        this.name = organization.getName();
        this.inn = organization.getInn();
        this.kpp = organization.getKpp();
        this.fias = organization.getFias();
        this.address = organization.getAddress();
        this.parent = organization.getParent() != null ? organization.getParent().getId() : null;
        this.type = organization.getType() != null ? organization.getType().getId() : null;
        this.smo = organization.getSmo() != null ? organization.getSmo().getId() : null;
        this.location = organization.getLocation() != null ? organization.getLocation().getId() : null;
        this.funCustomer = organization.getFunCustomer().getName();
    }
}
