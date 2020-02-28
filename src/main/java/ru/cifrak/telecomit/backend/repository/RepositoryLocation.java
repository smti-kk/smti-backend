package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;

import java.util.List;

@Repository
public interface RepositoryLocation extends JpaRepository<CatalogsLocation, Integer> {

    @Query("SELECT l from CatalogsLocation l where" +
            " l.typeLocation not like 'р-н' " +
            "or l.typeLocation not like 'край' " +
            "or l.typeLocation not like 'с/с' " +
            "or l.typeLocation not like 'тер' "
    )
    List<CatalogsLocation> locations();

    @Query("SELECT l from CatalogsLocation l where l.level = 1 ")
    List<CatalogsLocation> parents();
}
