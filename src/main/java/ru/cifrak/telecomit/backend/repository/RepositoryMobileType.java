package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TypeMobile;

@Repository
public interface RepositoryMobileType extends JpaRepository<TypeMobile, Integer> {

    TypeMobile findByName(String name);
}
