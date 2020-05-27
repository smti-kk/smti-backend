package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor

@Entity
@Table
public class Hardware implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HARDWARE_ID_GENERATOR", sequenceName = "hardware_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HARDWARE_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, length = 256)
    private String name;
}
