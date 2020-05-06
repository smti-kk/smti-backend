package ru.cifrak.telecomit.backend.entities;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class AuditingSoftDelete extends Auditing {
    @Column(name = "deleted")
    private Boolean deleted;
}
