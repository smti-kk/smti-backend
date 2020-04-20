package ru.cifrak.telecomit.backend.cache.service;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;
import ru.cifrak.telecomit.backend.cache.entity.TempTokenCache;
import ru.cifrak.telecomit.backend.cache.repository.TempTokenCacheRepository;
import ru.cifrak.telecomit.backend.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class TempTokenCacheServiceImpl implements TempTokenCacheService {
    private final TempTokenCacheRepository tempTokenCacheRepository;

    public TempTokenCacheServiceImpl(TempTokenCacheRepository tempTokenCacheRepository) {
        this.tempTokenCacheRepository = tempTokenCacheRepository;
    }

    @Override
    public TempTokenCache createFor(AuthTokenCache authTokenCache) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        final byte[] encodedHash = digest.digest(Long.toString(System.currentTimeMillis()).getBytes());

        final String token = Utils.bytesToHex(encodedHash);

        return tempTokenCacheRepository.save(new TempTokenCache(token, authTokenCache.getId()));
    }
}
