package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Changes;

@Repository
public interface RepositoryChanges extends JpaRepository<Changes, Integer> {
    Changes findByName(String name);
}
