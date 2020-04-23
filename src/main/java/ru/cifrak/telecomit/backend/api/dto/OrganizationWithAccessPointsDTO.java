package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganization;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganizationtype;
import ru.cifrak.telecomit.backend.domain.CatalogsSmotype;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationWithAccessPointsDTO {
    private Integer id;
    private String name;
    @JsonProperty("full_name")
    private String fullName;
    private String inn;
    private String kpp;
    private UUID fias;
    private String address;
    private Integer parent;
    private CatalogsOrganizationtype type;
    @JsonProperty("type_smo")
    private CatalogsSmotype smoType;
    private Integer location;
    private List<AccessPointFull> reaccesspoints;

    public OrganizationWithAccessPointsDTO(CatalogsOrganization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.fullName = organization.getFullName();
        this.inn = organization.getInn() != null ? organization.getInn().toString() : null;
        this.kpp = organization.getKpp() != null ? organization.getKpp().toString() : null;
        this.fias = organization.getFias();
        this.address = organization.getAddress();
        this.parent = organization.getParent() != null ? organization.getParent().getId() : null;
        this.type = organization.getType();
        this.smoType = organization.getSmoType();
        this.location = organization.getCatalogsLocation().getId();
        this.reaccesspoints = organization.getMonitoringAccesspointRes().stream().map(AccessPointFull::new).collect(Collectors.toList());
    }
}
