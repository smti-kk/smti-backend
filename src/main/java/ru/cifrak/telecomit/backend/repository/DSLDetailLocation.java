package ru.cifrak.telecomit.backend.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;
import ru.cifrak.telecomit.backend.entities.locationsummary.QDetailLocation;


public interface DSLDetailLocation extends JpaRepository<DetailLocation, Integer>,
        QuerydslPredicateExecutor<DetailLocation>, QuerydslBinderCustomizer<EntityPathBase<QDetailLocation>> {
    @Override
    default void customize(QuerydslBindings bindings, EntityPathBase<QDetailLocation> root) {
    }

    @Override
    @EntityGraph("detail-locations")
    @NotNull
    Page<DetailLocation> findAll(@NotNull Predicate predicate, @NotNull Pageable pageable);
}
