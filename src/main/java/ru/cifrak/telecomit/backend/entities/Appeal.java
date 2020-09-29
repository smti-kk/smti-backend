package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class Appeal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPEAL_ID_GENERATOR")
    @SequenceGenerator(name = "APPEAL_ID_GENERATOR", sequenceName = "appeal_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private AppealStatus status;

    @Column(nullable = false)
    private AppealPriority priority;

    @Column(nullable = false)
    private AppealLevel level;

    private Integer locationId;

    @Column(nullable = false)
    private LocalDate date;

    private String file;
    private String responseFile;

    @CreatedDate
    private LocalDate creationDate;

    @CreatedBy
    private String creator;
}

