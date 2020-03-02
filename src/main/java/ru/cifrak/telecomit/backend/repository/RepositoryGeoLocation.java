package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.domain.CatalogsGeolocation;

@Repository
public interface RepositoryGeoLocation extends JpaRepository<CatalogsGeolocation, Integer> {

}
