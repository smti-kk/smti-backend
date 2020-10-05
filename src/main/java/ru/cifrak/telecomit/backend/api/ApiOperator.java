package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.service.ServiceOperators;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/operator")
public class ApiOperator {
    private final ServiceOperators serviceOperators;
    private final LocationService locationService;

    public ApiOperator(ServiceOperators serviceOperators, LocationService locationService) {
        this.serviceOperators = serviceOperators;
        this.locationService = locationService;
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Operator> item(@NotNull @PathVariable Integer id) throws NotFoundException {
        Operator item = serviceOperators.item(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping
    public List<Operator> findAll() {
        return serviceOperators.findAll();
    }

    @GetMapping(params = {"page", "size"})
    public Page<Operator> findAll(Pageable pageable) {
        return serviceOperators.findAll(pageable);
    }

    @GetMapping("/grouped")
    public Map<String, List<Operator>> grouped() {
        return serviceOperators.grouped();
    }

    @GetMapping("/internet")
    public List<Operator> internet() {
        return serviceOperators.internet();
    }

    @GetMapping("/mobile")
    public List<Operator> mobile() {
        return serviceOperators.mobile();
    }

    @GetMapping("/postal")
    public List<Operator> postal() {
        return serviceOperators.postal();
    }

    @GetMapping("/ats")
    public List<Operator> ats() {
        log.info("->GET /api/operator/ats");
        return serviceOperators.ats();
    }

    @GetMapping("/radio")
    public List<Operator> radio() {
        log.info("->GET /api/operator/radio");
        return serviceOperators.radio();
    }

    @GetMapping("/tv")
    public List<Operator> television() {
        log.info("->GET /api/operator/tv");
        return serviceOperators.television();
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    @CacheEvict(value = "grouped_operators", allEntries = true)
    public Operator createOrUpdateOperator(@RequestBody Operator operator,
                                           @AuthenticationPrincipal User user) {
        log.info("user {} create new operator {}", user.getId(), operator.getName());
        return serviceOperators.save(operator);
    }

    @PostMapping("/add-icon")
    @Secured("ROLE_ADMIN")
    public Map<String, String> createOperatorIcon(@RequestParam(required = false) MultipartFile icon) {
        log.info("add new operator icon to database");
        HashMap<String, String> objWithIconPath = new HashMap<>();
        objWithIconPath.put("iconPath", serviceOperators.createIcon(icon));
        return objWithIconPath;
    }

    @DeleteMapping("/{operatorId}")
    @Secured("ROLE_ADMIN")
    public void deleteOperator(@PathVariable("operatorId") Integer id,
                               @AuthenticationPrincipal User user) {
        log.info("delete operator with id {} {}", id, user.getEmail());
        serviceOperators.delete(id);
        locationService.refreshCache();
    }
}
