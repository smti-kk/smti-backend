package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;

import java.util.List;

@Repository
public interface RepositoryGovernmentDevelopmentProgram extends JpaRepository<GovernmentDevelopmentProgram, Integer> {

     GovernmentDevelopmentProgram findByAcronym(String acronym);

     @Query("SELECT p.acronym from GovernmentDevelopmentProgram p")
     List<String> findAllAcronym();
}
