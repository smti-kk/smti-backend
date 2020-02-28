package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganization;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganizationtype;

@Repository
public interface RepositoryOrganizationType extends JpaRepository<CatalogsOrganizationtype, Integer> {
}
