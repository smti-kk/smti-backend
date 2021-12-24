package ru.cifrak.telecomit.backend.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;

import java.util.List;


public interface RepositoryAccessPointsFull extends JpaRepository<AccessPointFull, Integer>, JpaSpecificationExecutor<AccessPointFull> {
    @EntityGraph(AccessPointFull.REPORT_ALL)
    @Override
    @NotNull
    Page<AccessPointFull> findAll(Specification spec, @NotNull Pageable pageable);


    @EntityGraph(AccessPointFull.REPORT_ALL)
    @Override
    @NotNull
    List<AccessPointFull> findAll();

    @EntityGraph(AccessPointFull.REPORT_ALL_EXPORT)
    @Override
    @NotNull
    List<AccessPointFull> findAll(Specification specification, @NotNull Sort sort);

    @EntityGraph(AccessPointFull.REPORT_ALL_EXPORT)
    @Override
    @NotNull
    List<AccessPointFull> findAll(Specification specification);

}
