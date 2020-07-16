package ru.cifrak.telecomit.backend.repository;

import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.DLocationBase;
import ru.cifrak.telecomit.backend.entities.Location;

import java.util.List;

@Repository
public interface RepositoryDLocationBase extends JpaRepository<DLocationBase, Integer> {
    @EntityGraph(value = DLocationBase.WITH_PARENT)
    @Override
    List<DLocationBase> findAll();
}
