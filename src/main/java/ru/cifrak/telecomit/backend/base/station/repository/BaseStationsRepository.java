package ru.cifrak.telecomit.backend.base.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;


public interface BaseStationsRepository extends JpaRepository<BaseStation, Integer> {
}
