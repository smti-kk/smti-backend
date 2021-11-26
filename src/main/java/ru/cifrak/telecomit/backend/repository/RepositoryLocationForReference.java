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
    @NotNull
    Page<LocationForReference> findAll(@NotNull Predicate predicate, @NotNull Pageable pageable);

    @Override
    @EntityGraph("detail-locations")
    @NotNull
    Optional<LocationForReference> findById(@NotNull Integer id);
}
