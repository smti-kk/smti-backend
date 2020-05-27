package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.GeoData;

@Repository
public interface RepositoryGeoLocation extends JpaRepository<GeoData, Integer> {

}
