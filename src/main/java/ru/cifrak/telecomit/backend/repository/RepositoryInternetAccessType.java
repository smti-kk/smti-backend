package ru.cifrak.telecomit.backend.repository;

import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TypeInternetAccess;

import java.util.List;

@Repository
public interface RepositoryInternetAccessType extends JpaRepository<TypeInternetAccess, Integer> {

    @Nullable
    TypeInternetAccess findByName(String name);

    @Query("SELECT l.name from TypeInternetAccess l")
    List<String> findAllTypes();
}
