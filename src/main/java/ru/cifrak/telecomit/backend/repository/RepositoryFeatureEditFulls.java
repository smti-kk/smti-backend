package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEditFull;

@Repository
public interface RepositoryFeatureEditFulls extends JpaRepository<FeatureEditFull, Integer> {
    @Query("SELECT f " +
            "FROM FeatureEditFull f " +
            "WHERE f.locationFeaturesEditingRequest.status <> 'WAIT_FOR_STATE_TO_BE_SET' " +
            "AND f.locationFeaturesEditingRequest.status <> 'DECLINED'")
    Page<FeatureEditFull> findAllRequestsAndImportAndEditions(Pageable pageable);
}
