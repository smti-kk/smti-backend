package ru.cifrak.telecomit.backend.base.station.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;

import java.util.List;


public interface BaseStationsRepository extends JpaRepository<BaseStation, Integer> {
    @Override
    @EntityGraph("base_station_full")
    @NotNull
    List<BaseStation> findAll();
}
