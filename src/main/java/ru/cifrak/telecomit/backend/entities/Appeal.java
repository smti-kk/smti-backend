package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class Appeal implements Serializable {
    @Id
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private AppealStatus status;

    @Column(nullable = false)
    private AppealPriority priority;

    @Column(nullable = false)
    private AppealLevel level;

    @ManyToOne
    private Location location;

    @Column(nullable = false)
    private LocalDate date;

    private String file;
    private String responseFile;

    @CreatedDate
    private LocalDate creationDate;

    @CreatedBy
    private String creator;
}

