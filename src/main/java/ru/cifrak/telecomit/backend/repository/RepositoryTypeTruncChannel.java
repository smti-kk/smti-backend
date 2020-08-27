package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TypeSmo;
import ru.cifrak.telecomit.backend.entities.TypeTrunkChannel;

import java.util.List;

@Repository
public interface RepositoryTypeTruncChannel extends JpaRepository<TypeTrunkChannel, Integer> {
     List<TypeTrunkChannel> findByNameStartingWith(String name);

     TypeTrunkChannel findByName(String name);
}
