package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeLocation;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEditAction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/simple/")
public class ApiSimple {
    @GetMapping("/actions/")
    public FeatureEditAction[] getFeatureEditActions() {
        return FeatureEditAction.values();
    }

    @GetMapping("/type-locations/")
    public List<String> getTypeLocation() {
        return Arrays.stream(TypeLocation.values()).map(TypeLocation::getDescription).collect(Collectors.toList());
    }
}
