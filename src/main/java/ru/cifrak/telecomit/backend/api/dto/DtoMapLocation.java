package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;

@Data
public class DtoMapLocation {
    private MapLocation location;
    private String quality;

    public DtoMapLocation(MapLocation location, String quality) {
        this.location = location;
        this.quality = quality;
    }
}
