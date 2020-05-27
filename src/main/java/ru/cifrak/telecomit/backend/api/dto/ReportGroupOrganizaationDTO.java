package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.Organization;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReportGroupOrganizaationDTO {
    private Integer id;
    private String acronym;
    private String name;
    private TypeOrgDTO type;
    private TypeSmoDTO smo;
    private LocationSimpleFilterDTO location;
    private List<ReportAccessPointDTO> aps = new ArrayList<>();

    public ReportGroupOrganizaationDTO(Organization key, List<AccessPoint> value) {
        this.id = key.getId();
        this.acronym = key.getAcronym();
        this.name = key.getName();
        this.type = key.getType() != null ? new TypeOrgDTO(key.getType()) : null;
        this.smo = key.getSmo() != null ? new TypeSmoDTO(key.getSmo()) : null;
        this.location = key.getLocation() != null ? new LocationSimpleFilterDTO(key.getLocation()) : null;
        value.forEach(item -> this.aps.add(new ReportAccessPointDTO(item)));
    }
}
