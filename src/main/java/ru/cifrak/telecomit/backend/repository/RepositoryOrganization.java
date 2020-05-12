package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Organization;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryOrganization extends JpaRepository<Organization, Integer> {

    @EntityGraph(Organization.FULL)
    @Query(value = "SELECT co from Organization co where co.location.id = :locationId")
    List<Organization> findAllByLocationId(Integer locationId);

    @EntityGraph(Organization.FULL)
    @Override
    Optional<Organization> findById(Integer integer);
}
