package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DtoMapLocation {
    private Serializable location;
    private List<String> qualities;

    public DtoMapLocation(Serializable location, List<String> qualities) {
        this.location = location;
        this.qualities = qualities;
    }
}
