
package ru.cifrak.telecomit.backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryUser;
import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;
import ru.cifrak.telecomit.backend.cache.service.AuthTokenCacheService;
import ru.cifrak.telecomit.backend.entities.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/esia")
public class ApiEsia {

    private final RepositoryUser userRepository;

    private final AuthTokenCacheService authTokenCacheService;

    public ApiEsia(RepositoryUser userRepository, AuthTokenCacheService authTokenCacheService) {
        this.userRepository = userRepository;
        this.authTokenCacheService = authTokenCacheService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, Object> esia_data) {

        final ZoneId zoneId = ZoneId.systemDefault(); // TODO get from properties
        final LocalDateTime nowTime = LocalDateTime.now(zoneId);

        final long oid = ((Number) esia_data.getOrDefault("oid", 0)).longValue();
        Map<String, Object> info = (HashMap<String, Object>) esia_data.getOrDefault("info", new HashMap<String, Object>());
        Map<String, Object> addrs = (HashMap<String, Object>) esia_data.getOrDefault("addrs", new HashMap<String, Object>());
        Map<String, Object> ctts = (HashMap<String, Object>) esia_data.getOrDefault("ctts", new HashMap<String, Object>());
        Map<String, Object> docs = (HashMap<String, Object>) esia_data.getOrDefault("docs", new HashMap<String, Object>());
        Map<String, Object> orgs = (HashMap<String, Object>) esia_data.getOrDefault("orgs", new HashMap<String, Object>());
        Map<String, Object> vhls = (HashMap<String, Object>) esia_data.getOrDefault("vhls", new HashMap<String, Object>());

        if (info == null) { info = new HashMap<String, Object>(); }
        if (addrs == null) { addrs = new HashMap<String, Object>(); }
        if (ctts == null) { ctts = new HashMap<String, Object>(); }
        if (docs == null) { docs = new HashMap<String, Object>(); }
        if (orgs == null) { orgs = new HashMap<String, Object>(); }
        if (vhls == null) { vhls = new HashMap<String, Object>(); }

        final String firstName = (String) info.get("firstName");
        final String lastName = (String) info.getOrDefault("lastName", "");
        final String middleName = (String) info.getOrDefault("middleName", "");

        // TODO refactor this
        final String email = (String)((List)ctts.getOrDefault("elements", new ArrayList<>())).stream()
                .filter(m -> ((Map)m).getOrDefault("type", "").equals("EML"))
                .map(m -> ((Map)m).getOrDefault("value", ""))
                .findFirst().orElse("");

        final String phoneNumber = ((String)((List)ctts.getOrDefault("elements", new ArrayList<>())).stream()
                .filter(m -> ((Map)m).getOrDefault("type", "").equals("MBT"))
                .map(m -> ((Map)m).getOrDefault("value", ""))
                .findFirst().orElse(""))
                .replace("(", "").replace(")", "");

        final String passportData = (String)((List)docs.getOrDefault("elements", new ArrayList<>())).stream()
                .filter(m ->
                        ((Map)m).getOrDefault("type", "").equals("RF_PASSPORT")
                                || ((Map)m).getOrDefault("type", "").equals("FID_DOC")
                )
                .map(m -> (((Map)m).getOrDefault("series", "") + " " + ((Map)m).getOrDefault("number", "")).trim())
                .findFirst()
                .orElse("");

        final List<String> numberPlates = (List<String>) ((List)vhls.getOrDefault("elements", new ArrayList<>())).stream()
                .map(m -> ((String)((Map)m).getOrDefault("numberPlate", "")).trim())
                .filter(numberPlate -> !((String)numberPlate).isEmpty())
                .collect(Collectors.toList());

        final String username = String.format("%012d", oid);

        final Optional<User> optionalUser = userRepository.findByUsername(username);

        final User user = optionalUser.map(existingUser -> {
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setPatronymicName(middleName);
            existingUser.setPhone(phoneNumber);
            existingUser.setPassport(passportData);
            existingUser.setEmail(email);
            return userRepository.save(existingUser);
        }).orElseGet(() -> {
            final User newUser = new User();
            newUser.setUsername(username);
            newUser.setOid(oid);
            newUser.setPassword("");
            newUser.setCreateDateTime(nowTime);

            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setPatronymicName(middleName);
            newUser.setPhone(phoneNumber);
            newUser.setPassport(passportData);
            newUser.setEmail(email);
            return userRepository.save(newUser);
        });

        final AuthTokenCache authTokenCache = authTokenCacheService
                .findByUser(user)
                .orElseGet(() -> authTokenCacheService.createForUser(user, zoneId));

        return ResponseEntity.ok(new HashMap<String, String>() {{
            put("redirect_params", "");
            put("auth_token", authTokenCache.getId());
        }});
    }
}
