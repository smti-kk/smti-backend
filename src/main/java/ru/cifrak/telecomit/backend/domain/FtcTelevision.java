package ru.cifrak.telecomit.backend.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.cifrak.telecomit.backend.serializer.SignalConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the ftc_television database table.
 */
@Entity
@Table(name = "ftc_television")
@NamedQuery(name = "FtcTelevision.findAll", query = "SELECT f FROM FtcTelevision f")
@Data
@EqualsAndHashCode(callSuper = true)
public class FtcTelevision extends AccessPoint implements Serializable {
    @Column(nullable = false, length = 3)
    @Convert(converter = SignalConverter.class)
    private List<Signal> type;
}
