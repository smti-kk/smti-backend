package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Operator;

import java.util.List;

@Repository
public interface RepositoryOperator extends JpaRepository<Operator, Integer> {

    @Query("SELECT o from Operator o where o.services LIKE '%internet%' ")
    List<Operator> internet();

    @Query("SELECT o from Operator o where o.services LIKE '%mobile%' ")
    List<Operator> mobile();

    @Query("SELECT o from Operator o where o.services LIKE '%ats%' ")
    List<Operator> ats();

    @Query("SELECT o from Operator o where o.services LIKE '%tv%' ")
    List<Operator> television();

    @Query("SELECT o from Operator o where o.services LIKE '%radio%' ")
    List<Operator> radio();

    @Query("SELECT o from Operator o where o.services LIKE '%payphone%' ")
    List<Operator> payphone();

    @Query("SELECT o from Operator o where o.services LIKE '%infomat%' ")
    List<Operator> infomat();

    @Query("SELECT o from Operator o where o.services LIKE '%postal%' ")
    List<Operator> postal();

    Operator findByName(String name);
}
