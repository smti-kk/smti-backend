package ru.cifrak.telecomit.backend.cache.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.cache.entity.TempTokenCache;

@Repository
public interface TempTokenCacheRepository extends CrudRepository<TempTokenCache, String> {
}
