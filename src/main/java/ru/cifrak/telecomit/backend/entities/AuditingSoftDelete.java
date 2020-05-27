package ru.cifrak.telecomit.backend.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data

@MappedSuperclass
public class AuditingSoftDelete extends Auditing {
    @Column(name = "deleted")
    private Boolean deleted;
}
