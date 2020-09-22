package ru.cifrak.telecomit.backend.repository;

import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TypeInternetAccess;
import ru.cifrak.telecomit.backend.entities.TypeOrganization;

import java.util.List;

@Repository
public interface RepositoryOrganizationType extends JpaRepository<TypeOrganization, Integer> {

    @Nullable
    TypeOrganization findByName(String name);

    @Query("SELECT l.name from TypeOrganization l")
    List<String> findAllTypes();
}
