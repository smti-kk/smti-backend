package ru.cifrak.telecomit.backend.features.comparing;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationFC, Integer> {
    @NotNull
    @Cacheable("locations_fc")
    Page<LocationFC> findAll(@NotNull Pageable pageable);
}
