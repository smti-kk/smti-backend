package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.entities.TypeOrganization;
import ru.cifrak.telecomit.backend.entities.TypeSmo;

import java.util.List;
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
    private Integer location;

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
        this.location = organization.getLocation() != null ? organization.getLocation().getId() : null;
    }
}
