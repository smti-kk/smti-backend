package ru.cifrak.telecomit.backend.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;

@RequestMapping("/api/detail-locations")
public interface ApiLocationDetail {

    @GetMapping
    Page<DetailLocation> getList(Pageable pageable);
}
