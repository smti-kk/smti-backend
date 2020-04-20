package ru.cifrak.telecomit.backend.cache.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;

import java.util.Optional;

@Repository
public interface AuthTokenCacheRepository extends CrudRepository<AuthTokenCache, String> {
    Optional<AuthTokenCache> findByUserId(Long userId);
}
