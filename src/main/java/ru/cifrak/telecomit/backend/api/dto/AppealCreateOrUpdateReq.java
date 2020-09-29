package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AppealCreateOrUpdateReq implements Serializable {
    private Integer id;
    private String title;
    private AppealStatus status;
    private AppealPriority priority;
    private AppealLevel level;
    private Integer locationId;
    private LocalDate date;
}
