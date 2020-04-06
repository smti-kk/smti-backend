package ru.cifrak.telecomit.backend.repository;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT l from CatalogsLocation l where" +
            " l.typeLocation not in ('р-н', 'край', 'с/с', 'тер')" +
            "and within(l.catalogsGeolocation.administrativeCenter, :bbox) = true")
    List<CatalogsLocation> locationsByBbox(@Param("bbox") Polygon bbox);

    @Query(value = "SELECT l from CatalogsLocation l where" +
            " l.typeLocation not in ('р-н', 'край', 'с/с', 'тер')" +
            "and l.catalogsGeolocation.administrativeCenter is not NULL " +
            "and (l.parent.id = ?1 or l.id = ?1)")
    List<CatalogsLocation> findAllByParentId(Integer parentId);

    @Query(value = "SELECT l from CatalogsLocation l" +
            " where l.typeLocation not in ('р-н', 'край', 'с/с', 'тер')")
    Page<CatalogsLocation> findAll(Pageable pageable);
}
