package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.domain.CatalogsSmotype;

@Repository
public interface RepositoryLocation extends JpaRepository<CatalogsLocation, Integer> {
}
