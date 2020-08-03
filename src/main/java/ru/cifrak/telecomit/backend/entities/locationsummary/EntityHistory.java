package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;


@Data
@Embeddable
public class EntityHistory {
    @CreatedDate
    public LocalDateTime created;

    @LastModifiedDate
    public LocalDateTime modified;

    @CreatedBy
    public String createdBy;

    @LastModifiedBy
    public String modifiedBy;
}
