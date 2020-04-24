package ru.cifrak.telecomit.backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.AuthDTO;
import ru.cifrak.telecomit.backend.api.dto.TokenDTO;
import ru.cifrak.telecomit.backend.api.dto.User;
import ru.cifrak.telecomit.backend.auth.repository.UserRepository;
import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;
import ru.cifrak.telecomit.backend.cache.repository.TempTokenCacheRepository;
import ru.cifrak.telecomit.backend.cache.service.AuthTokenCacheService;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//@RestController
//@RequestMapping("/auth")
public class ApiEsiaExchanheToken {
    /*private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AuthTokenCacheService authTokenCacheService;

    private final TempTokenCacheRepository tempTokenCacheRepository;

    public ApiEsiaExchanheToken(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthTokenCacheService authTokenCacheService, TempTokenCacheRepository tempTokenCacheRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authTokenCacheService = authTokenCacheService;
        this.tempTokenCacheRepository = tempTokenCacheRepository;
    }

    @PostMapping("/login/")
    public ResponseEntity<TokenDTO> login(@Validated @RequestBody AuthDTO data) {
        final Optional<ru.cifrak.telecomit.backend.auth.entity.User> userOptional = userRepository.findByUsername(data.getUsername());

        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        final ru.cifrak.telecomit.backend.auth.entity.User user = userOptional.get();

        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        final Optional<AuthTokenCache> optionalAuthToken = authTokenCacheService.findByUser(user);

        if (optionalAuthToken.isPresent()) {
            return ResponseEntity.ok(new TokenDTO(optionalAuthToken.get().getId()));
        }

        final ZoneId zoneId = ZoneId.systemDefault(); // TODO get from properties

        final AuthTokenCache newAuthTokenCache = authTokenCacheService.createForUser(user, zoneId);

        return ResponseEntity.ok(new TokenDTO(newAuthTokenCache.getId()));
    }*/

    @PostMapping("/get_token/")
    public ResponseEntity<Map<String, Object>> exchangeTempToken(@RequestBody Map<String, String> data) {
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("token", "123");
        }});
    }

    @GetMapping("/account_info")
    public ResponseEntity<User> account_info() {
        return ResponseEntity.ok(new User("username123"));
    }
}