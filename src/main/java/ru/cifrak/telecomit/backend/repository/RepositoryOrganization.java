package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganization;

import java.util.List;

@Repository
public interface RepositoryOrganization extends JpaRepository<CatalogsOrganization, Integer> {
    List<CatalogsOrganization> findAllBy();

    @Query(value = "SELECT co from CatalogsOrganization co where co.catalogsLocation.id = :locationId")
    List<CatalogsOrganization> findAllByLocationId(Integer locationId);
}
