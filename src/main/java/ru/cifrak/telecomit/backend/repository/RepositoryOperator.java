package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.domain.CatalogsOperator;

import java.util.List;

@Repository
public interface RepositoryOperator extends JpaRepository<CatalogsOperator, Integer> {

    @Query("SELECT o from CatalogsOperator o where o.services LIKE '%internet%' ")
    List<CatalogsOperator> internet();

    @Query("SELECT o from CatalogsOperator o where o.services LIKE '%mobile%' ")
    List<CatalogsOperator> mobile();

    @Query("SELECT o from CatalogsOperator o where o.services LIKE '%ats%' ")
    List<CatalogsOperator> ats();

    @Query("SELECT o from CatalogsOperator o where o.services LIKE '%tv%' ")
    List<CatalogsOperator> television();

    @Query("SELECT o from CatalogsOperator o where o.services LIKE '%radio%' ")
    List<CatalogsOperator> radio();

    @Query("SELECT o from CatalogsOperator o where o.services LIKE '%postal%' ")
    List<CatalogsOperator> postal();

}
