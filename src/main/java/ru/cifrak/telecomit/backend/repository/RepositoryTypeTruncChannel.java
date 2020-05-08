package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TypeSmo;
import ru.cifrak.telecomit.backend.entities.TypeTrunkChannel;

@Repository
public interface RepositoryTypeTruncChannel extends JpaRepository<TypeTrunkChannel, Integer> {
}
