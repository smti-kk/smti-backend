package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.entities.OperatorService;

public interface RepositoryOperatorServices extends JpaRepository<OperatorService, Integer> {
}
