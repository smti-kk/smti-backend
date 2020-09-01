package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Getter;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@Table(name = "access_point")
public class AccessPointForTable implements Serializable {
    @Id
    private Integer id;
    private String type;

    @ManyToOne()
    @JoinColumn(name = "key_government_program")
    private GovernmentDevelopmentProgram governmentDevelopmentProgram;
}
