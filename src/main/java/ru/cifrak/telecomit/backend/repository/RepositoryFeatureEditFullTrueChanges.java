package ru.cifrak.telecomit.backend.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEditFullTrueChanges;

@Repository
public interface RepositoryFeatureEditFullTrueChanges extends JpaRepository<FeatureEditFullTrueChanges, Integer>,
        JpaSpecificationExecutor<FeatureEditFullTrueChanges> {

    @EntityGraph(FeatureEditFullTrueChanges.FULL)
    @Override
    @NotNull
    Page<FeatureEditFullTrueChanges> findAll(Specification spec, @NotNull Pageable pageable);
}
