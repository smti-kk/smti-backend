package ru.cifrak.telecomit.backend.features.comparing;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationFC, Integer>,
        QuerydslPredicateExecutor<LocationFC>, QuerydslBinderCustomizer<EntityPathBase<QLocationFC>> {

    @Override
    default void customize(QuerydslBindings bindings, EntityPathBase<QLocationFC> root) {
    }

    @NotNull
    @Cacheable("locations_fc")
    @Query("SELECT l from LocationFC l inner join LocationFC l2 on l.locationParent.id = l2.id where" +
            " l.type not like 'р-н' " +
            " and l.type not like 'край'" +
            " and l.type not like 'с/с'" +
            " and l.type not like 'тер'" +
            " and l.type not like 'округ'" +
            " order by l2.name, l.name"
    )
    Page<LocationFC> findAll(@NotNull Pageable pageable);

    @NotNull
    @Cacheable("locations_fc")
    Page<LocationFC> findAll(@NotNull Predicate predicate, @NotNull Pageable pageable);

    @NotNull
    @Cacheable("locations_fc")
    @Query("SELECT l" +
            " from LocationFC l inner join LocationFC l2 on l.locationParent.id = l2.id" +
            " inner join LocationFeature lf on l.id = lf.locationId" +
            " where lf.type = 'INET'" +
            " order by l2.name, l.name"
    )
    Page<LocationFC> findAllINET(@NotNull Pageable pageable);

    @NotNull
    @Cacheable("locations_fc")
    @Query("SELECT l" +
            " from LocationFC l inner join LocationFC l2 on l.locationParent.id = l2.id" +
            " inner join LocationFeature lf on l.id = lf.locationId" +
            " where lf.type = 'MOBILE'" +
            " order by l2.name, l.name"
    )
    Page<LocationFC> findAllMOBILE(@NotNull Pageable pageable);

    @Cacheable("locations_fc")
    @NotNull
    Iterable<LocationFC> findAll(@NotNull Predicate predicate);
}
