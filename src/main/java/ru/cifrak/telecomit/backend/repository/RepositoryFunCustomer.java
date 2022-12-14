package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.entities.FunCustomer;

import java.util.Optional;

public interface RepositoryFunCustomer extends JpaRepository<FunCustomer, Integer> {

    Optional<FunCustomer> findByName(String name);

}
