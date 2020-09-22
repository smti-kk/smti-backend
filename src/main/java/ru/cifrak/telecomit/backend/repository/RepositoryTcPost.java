package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TcMobile;
import ru.cifrak.telecomit.backend.entities.TcPost;

@Repository
public interface RepositoryTcPost extends JpaRepository<TcPost, Long>, JpaSpecificationExecutor {




}
