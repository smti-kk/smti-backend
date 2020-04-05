package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.domain.CatalogsGovernmentDevelopmentProgram;

@Repository
public interface RepositoryGovernmentProgram extends JpaRepository<CatalogsGovernmentDevelopmentProgram, Integer> {
}
