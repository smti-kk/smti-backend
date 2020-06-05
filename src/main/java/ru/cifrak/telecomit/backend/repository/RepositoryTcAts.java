package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.entities.TcAts;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryTcAts extends JpaRepository<TcAts, Long>, JpaSpecificationExecutor {




}
