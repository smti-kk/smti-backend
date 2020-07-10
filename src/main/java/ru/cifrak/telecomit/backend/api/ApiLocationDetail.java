package ru.cifrak.telecomit.backend.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;

import java.util.List;

@RequestMapping("/api/detail-locations")
public interface ApiLocationDetail {

    @GetMapping
    Page<DetailLocation> getList(Pageable pageable,
                                 @RequestParam(name = "mobile-type", required = false) List<Integer> typeMobiles,
                                 @RequestParam(name = "internet-type", required = false) List<Integer> internetTypes,
                                 @RequestParam(name = "internet-operators", required = false) List<Integer> internetOperators,
                                 @RequestParam(name = "cellular-operators", required = false) List<Integer> cellularOperators,
                                 @RequestParam(name = "is-logical-or", defaultValue = "true") Boolean isLogicalOr,
                                 @RequestParam(name = "location", required = false) String location,
                                 @RequestParam(name = "parent", required = false) String parent
    );
}
