package ru.cifrak.telecomit.backend.base.station.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.entities.TypeMobile;

import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Optional;


public interface BaseStationsRepository extends JpaRepository<BaseStation, Integer> {
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
