package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.Location;

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

    public LocationTcDTO(Location entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.type = entity.getType();
        this.parent = entity.getParent() != null ? entity.getParent().getType() + " " + entity.getParent().getName() : null;
        this.peopleCount = entity.getPopulation();
//        this.infomat = entity.getCatalogsInfomats().size();
    }
}
