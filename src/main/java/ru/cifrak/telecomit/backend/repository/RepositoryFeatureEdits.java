package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEdit;

public interface RepositoryFeatureEdits extends JpaRepository<FeatureEdit, Integer> {
}
