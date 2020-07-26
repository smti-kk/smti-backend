package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;

@Repository
public interface RepositoryWritableTc extends JpaRepository<WritableTc, Integer> {
}
