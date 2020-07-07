package ru.cifrak.telecomit.backend.repository;


import org.springframework.data.repository.Repository;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;

import java.util.List;

public interface RepositoryDetailLocation extends Repository<DetailLocation, Integer> {
    List<DetailLocation> findAll();
}
