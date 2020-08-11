package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.AuthFrontDTO;
import ru.cifrak.telecomit.backend.api.dto.TokenDTO;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryUser;
import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;
import ru.cifrak.telecomit.backend.cache.entity.TempTokenCache;
import ru.cifrak.telecomit.backend.cache.repository.TempTokenCacheRepository;
import ru.cifrak.telecomit.backend.cache.service.AuthTokenCacheService;
import ru.cifrak.telecomit.backend.entities.User;

import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthAPI {
    private final PasswordEncoder passwordEncoder;

    private final RepositoryUser userRepository;

    private final AuthTokenCacheService authTokenCacheService;

    private final TempTokenCacheRepository tempTokenCacheRepository;

    public AuthAPI(PasswordEncoder passwordEncoder, RepositoryUser userRepository, AuthTokenCacheService authTokenCacheService, TempTokenCacheRepository tempTokenCacheRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authTokenCacheService = authTokenCacheService;
        this.tempTokenCacheRepository = tempTokenCacheRepository;
    }

    @PostMapping("/login/")
    public ResponseEntity<TokenDTO> register(@Validated @RequestBody AuthFrontDTO data) throws NoSuchAlgorithmException {
        log.info("-> [api] auth/login/");
        final Optional<User> userOptional = userRepository.findByUsername(data.getEmail());

        if (!userOptional.isPresent()) {
            log.info("<- [api] auth/login/");
            return ResponseEntity.badRequest().build();
        }

        final User user = userOptional.get();

        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            log.info("<- [api] auth/login/");
            return ResponseEntity.badRequest().build();
        }

        final Optional<AuthTokenCache> optionalAuthToken = authTokenCacheService.findByUser(user);

        if (optionalAuthToken.isPresent()) {
            log.info("<- [api] auth/login/");
            return ResponseEntity.ok(new TokenDTO(optionalAuthToken.get().getId()));
        }

        final ZoneId zoneId = ZoneId.systemDefault(); // TODO get from properties

        final AuthTokenCache newAuthTokenCache = authTokenCacheService.createForUser(user, zoneId);
        log.info("<- [api] auth/login/");
        return ResponseEntity.ok(new TokenDTO(newAuthTokenCache.getId()));
    }

    @PostMapping("/exchange_temp_token")
    public ResponseEntity<TokenDTO> exchangeTempToken(@Validated @RequestBody TokenDTO data) {
        log.info("-> [api] auth/exchange_temp_token");
        final Optional<TempTokenCache> optionalTempTokenCache = tempTokenCacheRepository.findById(data.getToken());

        return optionalTempTokenCache
                .map(tempTokenCache -> {
                    final String token = tempTokenCache.getToken();
                    tempTokenCacheRepository.delete(tempTokenCache);
                    return ResponseEntity.ok(new TokenDTO(token));
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/account_info")
    public ResponseEntity<User> account_info(@AuthenticationPrincipal User user) {
        log.info("-> [api] auth/account_info");
        log.info("<- [api] auth/account_info");
        return ResponseEntity.ok(user);
    }
}
