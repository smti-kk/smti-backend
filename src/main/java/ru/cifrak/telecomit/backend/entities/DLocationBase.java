package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Data

@Entity
@Table(name = "location")
@Immutable
@NamedEntityGraph(
        name = DLocationBase.WITH_PARENT,
        attributeNodes = {
                @NamedAttributeNode("parent"),
        }
)
public class DLocationBase {
    public static final String WITH_PARENT = "d_location_base_parent";
    @Id
    private Integer id;
    private String name;
    private String type;
    @JsonIgnoreProperties("parent")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private DLocationBase parent;

}
