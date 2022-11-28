package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cifrak.telecomit.backend.entities.map.GeoDataShort;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LocationEm {

    private Integer locationId;
    private String fias;
    private String name;
    private String okato;
    private String oktmo;
    private Integer population;
    private String type;

    @Embedded
    private GeoDataEm geoData;

    @Embedded
    private LocationParentEm parent;

}
