package ru.cifrak.telecomit.backend.repository;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;


public interface RepositoryDetailLocation extends JpaRepository<DetailLocation, Integer> {

    /**
     * " WHERE exists(" +
     *             "   select stc FROM ShortTechnicalCapability stc WHERE stc.typeMobile is not null and l.id = stc.locationId" +
     *             ")" +
     */
    @EntityGraph("detail-locations")
    @Query("SELECT l" +
            " FROM DetailLocation l" +
            " WHERE" +
            " l.type not like 'р-н' " +
            " and l.type not like 'край' " +
            " and l.type not like 'с/с' " +
            " and l.type not like 'тер' "
    )
    @NotNull
    Page<DetailLocation> findAll(@NotNull Pageable pageable);
}
