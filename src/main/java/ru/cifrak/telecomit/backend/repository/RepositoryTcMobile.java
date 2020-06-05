package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TcInternet;
import ru.cifrak.telecomit.backend.entities.TcMobile;

@Repository
public interface RepositoryTcMobile extends JpaRepository<TcMobile, Long>, JpaSpecificationExecutor {




}
