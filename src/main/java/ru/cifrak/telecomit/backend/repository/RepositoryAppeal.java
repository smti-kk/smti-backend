package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.entities.Appeal;

public interface RepositoryAppeal extends JpaRepository<Appeal, Integer> {
}
