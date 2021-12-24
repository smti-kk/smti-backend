package ru.cifrak.telecomit.backend.auth.service;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryUser;
import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;
import ru.cifrak.telecomit.backend.cache.repository.AuthTokenCacheRepository;
import ru.cifrak.telecomit.backend.entities.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final AuthTokenCacheRepository authTokenCacheRepository;
    private final RepositoryUser userRepository;

    public UserServiceImpl(AuthTokenCacheRepository authTokenCacheRepository, RepositoryUser userRepository) {
        this.authTokenCacheRepository = authTokenCacheRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByToken(String token) {
        final Optional<AuthTokenCache> optionalAuthTokenCache = authTokenCacheRepository.findById(token);

        if (!optionalAuthTokenCache.isPresent()) {
            return Optional.empty();
        }

        final AuthTokenCache authToken = optionalAuthTokenCache.get();

        final Optional<User> optionalUser = userRepository.findById(authToken.getUserId());

        // Update authToken expire time // TODO move out to another service
//        if (optionalUser.isPresent()) {
//            authTokenCacheRepository.save(authToken);
//        }

        return optionalUser;

        //return authTokenCacheRepository.findById(token).flatMap(authToken -> userRepository.findById(authToken.getUserId()));
    }

    @Override
    public Optional<User> findByOid(Long oid) {
        return userRepository.findByOid(oid);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findDistinctByRolesInAndIsActiveTrueOrderById(Set<UserRole> roles) {
        return userRepository.findDistinctByRolesInAndIsActiveTrueOrderById(roles);
    };
}
