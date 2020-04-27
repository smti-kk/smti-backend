package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class LocationReportOrganizationDTO {
    private Integer                 id;
    private LocationSimpleFilterDTO parent;
    private List<OrganizationWithAccessPointsDTO> organizations;
    private String                  name;
    @JsonProperty("type_location")
    private String                  type;
    private String                  okato;
    private String                  oktmo;
    private UUID                    fias;
    @JsonProperty("people_count")
    private Integer                 people;

    public LocationReportOrganizationDTO(CatalogsLocation entity) {
        this.id = entity.getId();
        this.parent = entity.getParent() != null ? new LocationSimpleFilterDTO(entity.getParent()): null;
        this.organizations = entity.getCatalogsOrganizations() != null ? entity.getCatalogsOrganizations().stream()
                .map(OrganizationWithAccessPointsDTO::new)
                .collect(Collectors.toList()) : null;
        this.name = entity.getName();
        this.type = entity.getTypeLocation();
        this.okato = entity.getOkato();
        this.oktmo = entity.getOktmo();
        this.fias = entity.getFiasCode();
        this.people =entity.getPeopleCount();

    }
}
