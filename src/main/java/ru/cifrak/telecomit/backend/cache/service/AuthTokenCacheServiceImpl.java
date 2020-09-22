package ru.cifrak.telecomit.backend.cache.service;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;
import ru.cifrak.telecomit.backend.cache.repository.AuthTokenCacheRepository;
import ru.cifrak.telecomit.backend.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthTokenCacheServiceImpl implements AuthTokenCacheService {
    private final AuthTokenCacheRepository authTokenCacheRepository;

    public AuthTokenCacheServiceImpl(AuthTokenCacheRepository authTokenCacheRepository) {
        this.authTokenCacheRepository = authTokenCacheRepository;
    }

    @Override
    public Optional<AuthTokenCache> findByUser(User user) {
        return authTokenCacheRepository.findByUserId(user.getId());
    }

    @Override
    public AuthTokenCache createForUser(User user, ZoneId zoneId) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        final byte[] encodedHash = digest.digest(UUID.randomUUID().toString().getBytes());

        final String token = Utils.bytesToHex(encodedHash);

        return authTokenCacheRepository.save(new AuthTokenCache(token, user, zoneId));
    }
}
