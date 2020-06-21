package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;


public interface RepositoryJournalMAP extends JpaRepository<JournalMAP, Long>, JpaSpecificationExecutor {

}
