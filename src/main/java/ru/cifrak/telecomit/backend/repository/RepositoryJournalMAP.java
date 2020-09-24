package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;

import java.util.List;


public interface RepositoryJournalMAP extends JpaRepository<JournalMAP, Long>, JpaSpecificationExecutor {

    //TODO:[generate TICKET]: add entity graph
    List<JournalMAP> findAllByMap_IdAccountIn(List<Integer> accounts);

    JournalMAP findByAp_Id(Integer id);
}
