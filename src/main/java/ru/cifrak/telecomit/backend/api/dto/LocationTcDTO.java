package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

@Data
@AllArgsConstructor
public class LocationTcDTO {
    private Integer id;
    private String name;
    @JsonProperty("type_location")
    private String type;
    @JsonProperty("people_count")
    private Integer peopleCount;
    private String parent;
    private Integer infomat;

    public LocationTcDTO(CatalogsLocation entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.type = entity.getTypeLocation();
        this.parent = entity.getParent() != null ? entity.getParent().getTypeLocation() + " " + entity.getParent().getName() : null;
        this.peopleCount = entity.getPeopleCount();
        this.infomat = entity.getCatalogsInfomats().size();
    }
}
