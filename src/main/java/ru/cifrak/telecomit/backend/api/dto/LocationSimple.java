package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganization;

import java.util.List;

@Data
@AllArgsConstructor
public class LocationSimple {
    private Integer id;
    private String name;

    @JsonProperty("type_location")
    private String type;

    @JsonProperty("people_count")
    private Integer peopleCount;

    private CatalogsLocation parent;

//    @JsonProperty("parent")
    private String _municipalityAreaStr;

    private Integer infomat;

    private List<CatalogsOrganization> organizations;

}
