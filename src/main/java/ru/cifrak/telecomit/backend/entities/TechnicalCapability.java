package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)

@Entity
@Table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "split", discriminatorType = DiscriminatorType.STRING)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TechnicalCapability extends Auditing implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * COMMON FIELDS
     */
    @Id
    @SequenceGenerator(name = "TECHNICAL_CAPABILITY_GENERATOR", sequenceName = "technical_capability_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TECHNICAL_CAPABILITY_GENERATOR")
    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_location", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_operator", nullable = false)
    private Operator operator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceQuality quality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_request_for_change")
    private RequestForChange request;

    /**
     * GOVERNMENT DEVELOPMENT PROGRAM FIELDS
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_government_program")
    private GovernmentDevelopmentProgram govProgram;

    @Enumerated(EnumType.STRING)
    @Column
    private ParticipationStatus govStatus;

    @Column
    private Integer govYearComplete;

    @Column
    @Enumerated(EnumType.STRING)
    private TcState state;
}