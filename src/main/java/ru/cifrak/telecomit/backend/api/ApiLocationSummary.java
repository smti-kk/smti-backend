package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;

import java.util.List;

@RequestMapping(name = "/api/detail-locations")
public interface ApiLocationSummary {

    @GetMapping
    List<DetailLocation> getAll();
}
