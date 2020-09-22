package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;

import java.util.Set;

@Data
public class LocationFeaturesSaveRequest {
    private Set<WritableTc> features;
    private String comment;
}
