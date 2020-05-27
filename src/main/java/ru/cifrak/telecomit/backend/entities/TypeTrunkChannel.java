package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor

@Entity
@Table
@NamedQuery(name = "TypeTrunkChannel.findAll", query = "SELECT c FROM TypeTrunkChannel c")
public class TypeTrunkChannel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TYPETRUNKCHANNEL_ID_GENERATOR", sequenceName = "typetrunkchannel_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TYPETRUNKCHANNEL_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, length = 256)
    private String name;

}
