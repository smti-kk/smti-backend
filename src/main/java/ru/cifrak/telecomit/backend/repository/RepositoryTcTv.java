package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TcRadio;
import ru.cifrak.telecomit.backend.entities.TcTv;

@Repository
public interface RepositoryTcTv extends JpaRepository<TcTv, Long>, JpaSpecificationExecutor {




}
