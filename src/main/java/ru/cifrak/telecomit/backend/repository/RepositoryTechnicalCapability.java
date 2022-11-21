package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.entities.TechnicalCapability;

public interface RepositoryTechnicalCapability extends JpaRepository<TechnicalCapability, Long> {
}

