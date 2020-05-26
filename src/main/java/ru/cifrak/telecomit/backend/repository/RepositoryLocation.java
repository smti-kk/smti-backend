package ru.cifrak.telecomit.backend.repository;

import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Location;

import java.util.List;

@Repository
public interface RepositoryLocation extends JpaRepository<Location, Integer> {

    @EntityGraph(value = Location.WITH_FEATURES)
    @Query("SELECT l FROM Location l where l.id = :id")
    Location get(@NotNull Integer id);

    @Query("SELECT l from Location l where" +
            " l.type not like 'с/с' " +
            "and l.type not like 'тер' " +
            "and LOWER(l.name) like CONCAT('%',LOWER(:name),'%')"
    )
    List<Location> locations(@Param("name")String name);

    @Query("SELECT l from Location l where" +
            " l.type not like 'р-н' " +
            "and l.type not like 'край' " +
            "and l.type not like 'с/с' " +
            "and l.type not like 'тер' "
    )
    List<Location> locationFilter();

    @Query("SELECT l from Location l left join GeoData g on l.geoData.id = g.id where l.level = 1")
    List<Location> areaBorders();

    @Query("SELECT l from Location l where l.level = 1 ")
    List<Location> parents();

    @Query(value = "SELECT l from Location l where" +
            " l.type not in ('р-н', 'край', 'с/с', 'тер')" +
            "and within(l.geoData.administrativeCenter, :bbox) = true")
    List<Location> locationsByBbox(@Param("bbox") Polygon bbox);

    @EntityGraph(value = Location.WITH_FEATURES)
    @Query(value = "SELECT l from Location l where" +
            " l.type not in ('р-н', 'край', 'с/с', 'тер')" +
            "and l.geoData.administrativeCenter is not NULL " +
            "and (l.parent.id = ?1 or l.id = ?1)")
    List<Location> findAllByParentId(Integer parentId);

    @EntityGraph(value = Location.WITH_FEATURES)
    @Query(value = "SELECT l from Location l" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер') and l.name like '%Орловк%'"
    )
    Page<Location> findAll(Pageable pageable);

    //TODO: REMOVE!!!!!!! sql - and l.name like '%Орловк%'
    @EntityGraph(value = Location.WITH_ORGANIZATIONS_ACCESSPOINTS)
    @Query(value = "SELECT l from Location l" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер') and l.name like '%Орловк%'"
    )
    Page<Location> findAllReportOrganization(Pageable pageable);
}
