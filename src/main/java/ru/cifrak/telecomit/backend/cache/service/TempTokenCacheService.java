package ru.cifrak.telecomit.backend.cache.service;

import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;
import ru.cifrak.telecomit.backend.cache.entity.TempTokenCache;

public interface TempTokenCacheService {
    TempTokenCache createFor(AuthTokenCache authTokenCache);
}
