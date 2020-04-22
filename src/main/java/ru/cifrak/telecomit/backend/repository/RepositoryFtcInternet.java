package ru.cifrak.telecomit.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.domain.FtcInternet;

import java.util.List;

public interface RepositoryFtcInternet extends JpaRepository<FtcInternet, Integer> {

    @Query(value = "SELECT fi from FtcInternet fi where fi.catalogsLocation.id = ?1")
    List<FtcInternet> getByLocationId(Integer locationId);
}
