package ru.cifrak.telecomit.backend.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.Organization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepositoryOrganization extends JpaRepository<Organization, Integer>, JpaSpecificationExecutor {

    @EntityGraph(Organization.FULL)
    @Query(value = "SELECT co from Organization co where co.location.id = :locationId")
    Page<Organization> findAllByLocationId(Integer locationId, Pageable pageable);

    @EntityGraph(Organization.FULL)
    @Query(value = "SELECT co FROM Organization co" +
            " LEFT JOIN AccessPoint ap ON ap.organization.id = co.id" +
            " WHERE co.location.id = :locationId" +
            " AND ap.id <> :accessPointId")
    Page<Organization> findAllByLocationIdAndWithoutAccessPoint(
            Integer locationId,
            Integer accessPointId,
            Pageable pageable
    );

    @EntityGraph(Organization.FULL)
    @Query(value = "SELECT co FROM Organization co" +
            " LEFT JOIN AccessPoint ap ON ap.organization.id = co.id" +
            " WHERE co.location.id = :locationId" +
            " AND ap.id = :accessPointId")
    Organization findByLocationIdAndWithAccessPoint(
            Integer locationId,
            Integer accessPointId
    );

    List<Organization> findAll();

    Integer countAllByLocationId(Integer locationId);

    @EntityGraph(Organization.FULL)
    @Override
    Optional<Organization> findById(Integer integer);

    @Nullable
    Organization findByFias(UUID fias);

    /*@EntityGraph(Organization.REPORT_AP_ALL)
    @Override
    Page<Organization> findAll(Pageable pageable);

    @EntityGraph(Organization.REPORT_AP_ALL)
    @Override
    Page findAll(Specification spec, Pageable pageable);*/
}
