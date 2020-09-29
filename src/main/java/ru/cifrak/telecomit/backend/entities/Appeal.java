package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @ManyToOne
    private Location location;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    private DBFile file;

    @ManyToOne(fetch = FetchType.EAGER)
    private DBFile responseFile;

    @CreatedDate
    private LocalDate creationDate;

    @CreatedBy
    private String creator;

    public Appeal() {
    }

    public Appeal(Integer id,
                  String title,
                  AppealStatus status,
                  AppealPriority priority,
                  AppealLevel level,
                  Location location,
                  LocalDate date,
                  DBFile appealFile,
                  DBFile appealResponseFile) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.level = level;
        this.location = location;
        this.date = date;
        this.file = appealFile;
        this.responseFile = appealResponseFile;
    }
}

