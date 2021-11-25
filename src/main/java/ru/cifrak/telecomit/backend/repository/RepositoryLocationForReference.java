package ru.cifrak.telecomit.backend.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForReference;
import ru.cifrak.telecomit.backend.entities.locationsummary.QLocationForReference;

import java.util.Optional;


public interface RepositoryLocationForReference extends JpaRepository<LocationForReference, Integer>,
        QuerydslPredicateExecutor<LocationForReference>, QuerydslBinderCustomizer<EntityPathBase<QLocationForReference>> {
    @Override
    default void customize(QuerydslBindings bindings, EntityPathBase<QLocationForReference> root) {
    }

    @Override
    @EntityGraph("detail-locations")
    @NotNull
    Iterable<LocationForReference> findAll(@NotNull Predicate predicate);

    @Override
//    @EntityGraph("detail-locations-for-reference")
    @NotNull
    Page<LocationForReference> findAll(@NotNull Predicate predicate, @NotNull Pageable pageable);

    @Override
    @EntityGraph("detail-locations")
    @NotNull
    Optional<LocationForReference> findById(@NotNull Integer id);

//    @EntityGraph("detail-locations")
//    @NotNull
//    @Query("  SELECT distinct l FROM LocationForReference l where " +
//            " (exists (SELECT 1 FROM User u where u.id = :userId " +
//            "           and l.id in (select ul.id from u.locations ul)) " +
//            " or exists (SELECT 1 FROM User u where u.id = :userId " +
//            "           and l.locationParent in (select ul.id from u.locations ul))) " +
//            "           and l.type not like 'с/с' and l.type not like 'р-н' and l.type not like 'край' and l.type not like 'тер' and l.type not like 'округ' " +
//            " order by l.locationParent.id, l.name")
//    List<LocationForReference> findByUserId(@NotNull Long userId);

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM LocationForReference l WHERE l.id = ?1")
//    void forceDeleteById(Integer locationId);

//    @Transactional
//    @Modifying
//    @Query(value = "UPDATE public.location SET population = :population, parent_id = :parent WHERE id = :id", nativeQuery = true)
//    void updatePopulationAndParent(Integer id, Integer population, Integer parent);
}
