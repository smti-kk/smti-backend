package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.domain.CatalogsGovernmentdevelopmentprogram;
import ru.cifrak.telecomit.backend.domain.CatalogsInternetaccesstype;

@Repository
public interface RepositoryGovernmentProgram extends JpaRepository<CatalogsGovernmentdevelopmentprogram, Integer> {
}
