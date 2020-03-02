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
            "and l.typeLocation not like 'край' " +
            "and l.typeLocation not like 'с/с' " +
            "and l.typeLocation not like 'тер' "
    )
    List<CatalogsLocation> locations();

    @Query("SELECT l from CatalogsLocation l left join CatalogsGeolocation g on l.catalogsGeolocation.id = g.id where l.level = 1")
    List<CatalogsLocation> areaBorders();

    @Query("SELECT l from CatalogsLocation l where l.level = 1 ")
    List<CatalogsLocation> parents();
}
