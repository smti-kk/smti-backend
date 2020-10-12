package ru.cifrak.telecomit.backend.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryAccount extends JpaRepository<Account, Long> {

    @EntityGraph(Account.WITH_ALL)
    Page<Account> findAllByUsernameIsNot(String name, Pageable pageable);

    @EntityGraph(Account.WITH_ALL)
    @Override
    Optional<Account> findById(Long aLong);
}
