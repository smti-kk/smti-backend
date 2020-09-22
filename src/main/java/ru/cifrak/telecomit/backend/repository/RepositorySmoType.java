package ru.cifrak.telecomit.backend.repository;

import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.TypeInternetAccess;
import ru.cifrak.telecomit.backend.entities.TypeSmo;

import java.util.List;

@Repository
public interface RepositorySmoType extends JpaRepository<TypeSmo, Integer> {

    @Nullable
    TypeSmo findByName(String name);

    @Query("SELECT l.name from TypeSmo l")
    List<String> findAllTypes();
}
