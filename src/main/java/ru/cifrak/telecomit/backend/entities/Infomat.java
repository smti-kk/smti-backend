package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor

@Entity
@Table
@NamedQuery(name = "CatalogsInfomat.findAll", query = "SELECT c FROM Infomat c")
public class Infomat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INFOMAT_ID_GENERATOR", sequenceName = "infomat_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INFOMAT_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(columnDefinition = "text")
    private String address;

    @Column(columnDefinition = "text")
    private String contacts;

    @Column(nullable = false)
    private LocalDate installDate;

    //TODO: возможно тут надо реальной организацией а не строчкой
    @Column(length = 256)
    private String organization;

    @Column(nullable = false)
    private Boolean removed;

    @Column(length = 16)
    private String serialNumber;

    @Column(nullable = false)
    private double speed;

    //bi-directional many-to-one association to CatalogsLocation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_location", nullable = false)
    private Location location;

    //bi-directional many-to-one association to CatalogsTrunkchanneltype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_trunk_channel_type")
    private TypeTrunkChannel type;


}