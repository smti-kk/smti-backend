package ru.cifrak.telecomit.backend.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEditFull;

@Repository
public interface RepositoryFeatureEditFulls extends JpaRepository<FeatureEditFull, Integer>, JpaSpecificationExecutor<FeatureEditFull> {
    @Query("SELECT f " +
            "FROM FeatureEditFull f " +
            "WHERE f.locationFeaturesEditingRequest.status <> 'WAIT_FOR_STATE_TO_BE_SET' " +
            "AND f.locationFeaturesEditingRequest.status <> 'DECLINED' " +
            "AND (NOT f.action = 'UPDATE' OR f.action = 'UPDATE' AND (" +
            "((f.tc.type = 'PAYPHONE' OR f.tc.type = 'INFOMAT') AND NOT f.tc.payphones = f.newValue.payphones) " +
            "OR (f.tc.type = 'INET' AND (NOT f.tc.trunkChannel = f.newValue.trunkChannel OR NOT f.tc.quality = f.newValue.quality)) " +
            "OR (f.tc.type = 'MOBILE' AND (NOT f.tc.typeMobile = f.newValue.typeMobile OR NOT f.tc.quality = f.newValue.quality)) " +
            "OR (f.tc.type = 'POST' AND NOT f.tc.typePost = f.newValue.typePost) " +
            "OR ((f.tc.type = 'RADIO' OR f.tc.type = 'TV') AND NOT f.tc.tvOrRadioTypes = f.newValue.tvOrRadioTypes) " +
            "OR (f.tc.type = 'ATS')))")
    Page<FeatureEditFull> findAllRequestsAndImportAndEditions(Pageable pageable);

    @EntityGraph(FeatureEditFull.FULL)
    @Override
    @NotNull
    Page<FeatureEditFull> findAll(Specification spec, @NotNull Pageable pageable);
}
