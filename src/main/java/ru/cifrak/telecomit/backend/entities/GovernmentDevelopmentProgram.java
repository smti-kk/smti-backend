package ru.cifrak.telecomit.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table
public class GovernmentDevelopmentProgram implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GOVERNMENTDEVELOPMENTPROGRAM_ID_GENERATOR", sequenceName = "governmentdevelopmentprogram_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GOVERNMENTDEVELOPMENTPROGRAM_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 1000)
    private String name;

    @Column(nullable = false, length = 100)
    private String acronym;
}
