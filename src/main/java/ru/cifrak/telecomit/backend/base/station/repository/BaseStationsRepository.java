package ru.cifrak.telecomit.backend.base.station.repository;

import com.querydsl.core.types.dsl.EntityPathBase;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.base.station.entity.QBaseStation;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.entities.TypeMobile;
import ru.cifrak.telecomit.backend.entities.locationsummary.QLocationForTable;

import java.util.List;
import java.util.Optional;


public interface BaseStationsRepository extends JpaRepository<BaseStation, Integer>,
        QuerydslPredicateExecutor<BaseStation>, QuerydslBinderCustomizer<EntityPathBase<QBaseStation>> {

    @Override
    default void customize(QuerydslBindings bindings, EntityPathBase<QBaseStation> root) {
    }

    @Override
    @NotNull
    List<BaseStation> findAll();

    @Override
    @NotNull
    Optional<BaseStation> findById(@NotNull Integer integer);

    List<BaseStation> findByPointAndOperatorAndMobileType(
            Point point,
            Operator operator,
            TypeMobile typeMobile);
}
