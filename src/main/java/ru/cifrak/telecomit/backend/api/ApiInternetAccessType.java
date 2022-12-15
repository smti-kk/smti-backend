package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.entities.TypeAccessPoint;
import ru.cifrak.telecomit.backend.entities.TypeInternetAccess;
import ru.cifrak.telecomit.backend.repository.RepositoryInternetAccessType;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/type/internet-access")
public class ApiInternetAccessType {
    private RepositoryInternetAccessType repository;

    public ApiInternetAccessType(RepositoryInternetAccessType repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<TypeInternetAccess> list(@RequestParam(name = "apType", required = false) String apType) {
        log.info("-> GET /api/type/internet-access/");
        List<TypeInternetAccess> result = repository.findAll().stream().filter(typeInternetAccess ->
                apType == null || (typeInternetAccess.getApType().getName().equals(apType) ||
                        typeInternetAccess.getApType().getName().equals(TypeAccessPoint.GENERAL.getName()))
        ).collect(Collectors.toList());
        log.info("<- GET /api/type/internet-access/");
        return result;
    }

    @GetMapping("/{id}/")
    public TypeInternetAccess one(@PathVariable Integer id) {
        log.info("->GET /api/type/internet-access::{}",id);
        log.info("<- GET /api/type/internet-access::{}",id);
        return repository.findById(id).orElse(null);
    }
}
