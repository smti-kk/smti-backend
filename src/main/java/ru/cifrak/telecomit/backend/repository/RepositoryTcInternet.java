package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TcAts;
import ru.cifrak.telecomit.backend.entities.TcInternet;

@Repository
public interface RepositoryTcInternet extends JpaRepository<TcInternet, Long>, JpaSpecificationExecutor {




}
