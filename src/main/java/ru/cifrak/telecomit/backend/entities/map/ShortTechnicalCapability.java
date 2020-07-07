package ru.cifrak.telecomit.backend.entities.map;


import lombok.Getter;
import org.springframework.data.annotation.Immutable;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;

import javax.persistence.*;

@Entity
@Table(name = "technical_capability")
@Getter
@Immutable
public class ShortTechnicalCapability {
    @Id
    private Long id;

    @Column(name = "key_operator")
    private Integer operatorId;

    @Column(name = "split")
    private String type;

    @ManyToOne
    @JoinColumn(name = "key_government_program")
    private GovernmentDevelopmentProgram governmentDevelopmentProgram;

    public ShortTechnicalCapability() {
    }
}
