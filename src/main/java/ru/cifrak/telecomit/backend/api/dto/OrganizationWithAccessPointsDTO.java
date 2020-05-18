package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.Organization;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationWithAccessPointsDTO {
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
    private LocationSimpleFilterDTO location;
    private List<AccessPointDTOReportOgranization> accesspoints;

    public OrganizationWithAccessPointsDTO(Organization organization) {
        this.id = organization.getId();
        this.acronym = organization.getAcronym();
        this.name = organization.getName();
        this.inn = organization.getInn() != null ? organization.getInn().toString() : null;
        this.kpp = organization.getKpp() != null ? organization.getKpp().toString() : null;
        this.fias = organization.getFias();
        this.address = organization.getAddress();
        this.parent = organization.getParent() != null ? organization.getParent().getId() : null;
        this.type = organization.getType() != null ? new TypeOrgDTO(organization.getType()) : null;
        this.smo = organization.getSmo() != null ? new TypeSmoDTO(organization.getSmo()) : null;
        this.location = organization.getLocation() != null ? new LocationSimpleFilterDTO(organization.getLocation()) : null;
        this.accesspoints = organization.getAccessPoints().stream().map(AccessPointDTOReportOgranization::new).collect(Collectors.toList());
    }

    public OrganizationWithAccessPointsDTO(AccessPoint accessPoint) {
    }
}
