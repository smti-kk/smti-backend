package ru.cifrak.telecomit.backend.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.locationsummary.QLocationForTable;

import java.util.List;
import java.util.Optional;


public interface DSLDetailLocation extends JpaRepository<LocationForTable, Integer>,
        QuerydslPredicateExecutor<LocationForTable>, QuerydslBinderCustomizer<EntityPathBase<QLocationForTable>> {
    @Override
    default void customize(QuerydslBindings bindings, EntityPathBase<QLocationForTable> root) {
    }

    @Override
    @EntityGraph("detail-locations")
    @NotNull
    Page<LocationForTable> findAll(@NotNull Predicate predicate, @NotNull Pageable pageable);

    @Override
    @EntityGraph("detail-locations")
    @NotNull
    Optional<LocationForTable> findById(@NotNull Integer id);

    @EntityGraph("detail-locations")
    @NotNull
    @Query("  SELECT distinct l FROM LocationForTable l where " +
            " (exists (SELECT 1 FROM User u where u.id = :userId " +
            "           and l.id in (select ul.id from u.locations ul)) " +
            " or exists (SELECT 1 FROM User u where u.id = :userId " +
            "           and l.locationParent in (select ul.id from u.locations ul))) " +
            "           and l.type not like 'с/с' and l.type not like 'р-н' and l.type not like 'край' and l.type not like 'тер' " +
            " order by l.locationParent.id, l.name")
    List<LocationForTable> findByUserId(@NotNull Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM LocationForTable l WHERE l.id = ?1")
    void forceDeleteById(Integer locationId);

    @Transactional
    @Modifying
    @Query("UPDATE LocationForTable l SET l.population = :population WHERE l.id = :id")
    void updatePopulation(Integer id, Integer population);
}
