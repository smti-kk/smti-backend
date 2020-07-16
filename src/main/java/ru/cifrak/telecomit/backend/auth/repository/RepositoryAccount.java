package ru.cifrak.telecomit.backend.auth.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryAccount extends JpaRepository<Account, Long> {

    @EntityGraph(Account.WITH_ALL)
    @Override
    List<Account> findAll();

    @EntityGraph(Account.WITH_ALL)
    @Override
    Optional<Account> findById(Long aLong);
}
