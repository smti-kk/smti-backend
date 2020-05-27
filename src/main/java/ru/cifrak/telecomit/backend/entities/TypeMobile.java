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
@NamedQuery(name = "TypeMobile.findAll", query = "SELECT c FROM TypeMobile c")
public class TypeMobile implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TYPEMOBILE_ID_GENERATOR", sequenceName = "typemobile_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TYPEMOBILE_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, length = 256)
    private String name;

/*
	//bi-directional many-to-one association to TcMobile
	@OneToMany(mappedBy="catalogsMobiletype")
	private List<TcMobile> ftcMobiles;
*/
}