package ru.cifrak.telecomit.backend.repository;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.ApContract;

import java.util.List;


public interface RepositoryApContract extends JpaRepository<ApContract, Integer>, JpaSpecificationExecutor {

//    @EntityGraph(AccessPoint.REPORT_ALL)
    @Override
    Page findAll(Specification spec, Pageable pageable);
}
