package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Organization;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryOrganization extends JpaRepository<Organization, Integer>, JpaSpecificationExecutor {

    @EntityGraph(Organization.FULL)
    @Query(value = "SELECT co from Organization co where co.location.id = :locationId")
    List<Organization> findAllByLocationId(Integer locationId);

    @EntityGraph(Organization.FULL)
    @Override
    Optional<Organization> findById(Integer integer);

    /*@EntityGraph(Organization.REPORT_AP_ALL)
    @Override
    Page<Organization> findAll(Pageable pageable);

    @EntityGraph(Organization.REPORT_AP_ALL)
    @Override
    Page findAll(Specification spec, Pageable pageable);*/
}
