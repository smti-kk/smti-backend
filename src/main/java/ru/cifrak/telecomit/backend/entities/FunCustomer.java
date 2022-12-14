package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cifrak.telecomit.backend.api.dto.FunCustomerDto;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FunCustomer {

    @Id
    @SequenceGenerator(name = "FUN_CUSTOMER_ID_GENERATOR", sequenceName = "fun_customer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FUN_CUSTOMER_ID_GENERATOR")
    private Integer id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeAccessPoint apType;

    @JsonIgnore
    public FunCustomer(FunCustomerDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.apType = dto.getApType() != null && !dto.getApType().isEmpty() ?
                TypeAccessPoint.valueOf(dto.getApType()) : null;
    }

}
