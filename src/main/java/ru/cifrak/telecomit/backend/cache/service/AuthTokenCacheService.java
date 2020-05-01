package ru.cifrak.telecomit.backend.cache.service;

import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;

import java.time.ZoneId;
import java.util.Optional;

public interface AuthTokenCacheService {
    Optional<AuthTokenCache> findByUser(User user);
    AuthTokenCache createForUser(User user, ZoneId zoneId);
}
