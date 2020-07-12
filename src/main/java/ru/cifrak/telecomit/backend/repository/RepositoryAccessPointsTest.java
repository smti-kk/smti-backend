package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.entities.Location;

import java.util.List;

public interface RepositoryAccessPointsTest extends JpaRepository<Location, Integer> {

    @Query("SELECT l FROM Location l" +
            " LEFT JOIN FETCH l.organizations o" +
            " where o.id = 1"
    )
    List<Location> findAll();
}
