package ru.cifrak.telecomit.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "changes")
public class Changes implements Serializable {

    @Id
    @SequenceGenerator(name = "CHANGES_ID_GENERATOR", sequenceName = "changes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANGES_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeAccessPoint apType;
}
