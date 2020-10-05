package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.OperatorService;
import ru.cifrak.telecomit.backend.repository.RepositoryOperatorServices;

import java.util.List;

@RestController
@RequestMapping("/api/operator-services")
public class ApiOperatorServices {
    private final RepositoryOperatorServices repositoryOperatorServices;

    public ApiOperatorServices(RepositoryOperatorServices repositoryOperatorServices) {
        this.repositoryOperatorServices = repositoryOperatorServices;
    }

    @GetMapping
    public List<OperatorService> operatorServices() {
        return repositoryOperatorServices.findAll();
    }
}
