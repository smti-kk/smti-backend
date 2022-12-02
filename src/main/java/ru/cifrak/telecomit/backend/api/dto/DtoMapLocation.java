package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Immutable
@Table(name = "view_map_location")
@Subselect(" SELECT row_number() over () as id,\n" +
        "                    l.id as location_id,\n" +
        "                   l.fias as fias,\n" +
        "                   l.level as level,\n" +
        "                   l.name as name,\n" +
        "                   l.okato as okato,\n" +
        "                   l.oktmo as oktmo,\n" +
        "                   l.population as population,\n" +
        "                   l.type as type,\n" +
        "                   gd.id as geo_id,\n" +
        "                   gd.administrative_center as administrative_center,\n" +
        "                   l.parent_id as parent_id,\n" +
        "                   p.name as parent_name,\n" +
        "                   p.type as parent_type,\n" +
        "                   p.level as parent_level,\n" +
        "                   (select string_agg(tc.quality, ', ') from technical_capability tc where tc.split = 'MOBILE' and tc.state = 'ACTIVE' and tc.key_location = l.id) as qualities\n" +
        "            FROM location l\n" +
        "                     inner join technical_capability tc on l.id = tc.key_location\n" +
        "                     inner join geo_data gd on gd.id = l.geo_data_id\n" +
        "                     inner join location p on p.id = l.parent_id\n" +
        "            where l.type not in ('р-н', 'край', 'с/с', 'тер', 'мо')\n" +
        "              and l.geo_data_id is not null\n" +
        "            group by l.id, gd.id, p.id\n" +
        "            order by l.id")

public class DtoMapLocation implements Serializable {
    @Id
    private Integer id;

    @Column(name = "location")
    @Embedded
    private LocationEm location;

    @Column(name = "qualities")
    private String qualities;

    public DtoMapLocation() {
    }
}
