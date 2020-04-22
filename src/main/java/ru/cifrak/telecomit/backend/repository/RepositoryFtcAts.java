package ru.cifrak.telecomit.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.domain.FtcAts;

import java.util.List;

public interface RepositoryFtcAts extends JpaRepository<FtcAts, Integer> {
    @Query(value = "SELECT fi from FtcAts fi where fi.catalogsLocation.id = ?1")
    List<FtcAts> getByLocationId(Integer locationId);
}
