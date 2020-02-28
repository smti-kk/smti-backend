package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.domain.CatalogsInternetaccesstype;
import ru.cifrak.telecomit.backend.domain.CatalogsSmotype;

@Repository
public interface RepositoryInternetAccessType extends JpaRepository<CatalogsInternetaccesstype, Integer> {
}
