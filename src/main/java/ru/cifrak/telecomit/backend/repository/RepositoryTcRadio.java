package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TcPost;
import ru.cifrak.telecomit.backend.entities.TcRadio;

@Repository
public interface RepositoryTcRadio extends JpaRepository<TcRadio, Long>, JpaSpecificationExecutor {




}
