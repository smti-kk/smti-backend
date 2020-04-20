package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.cifrak.telecomit.backend.auth.entity.User;
import ru.cifrak.telecomit.backend.auth.entity.UserRole;
import ru.cifrak.telecomit.backend.auth.esia.EsiaAuth;
import ru.cifrak.telecomit.backend.auth.esia.EsiaConfig;
import ru.cifrak.telecomit.backend.auth.esia.EsiaInformationConnector;
import ru.cifrak.telecomit.backend.auth.esia.EsiaProperties;
import ru.cifrak.telecomit.backend.auth.repository.UserRepository;
import ru.cifrak.telecomit.backend.cache.entity.AuthTokenCache;
import ru.cifrak.telecomit.backend.cache.entity.TempTokenCache;
import ru.cifrak.telecomit.backend.cache.service.AuthTokenCacheService;
import ru.cifrak.telecomit.backend.cache.service.TempTokenCacheService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/esia-auth")
public class EsiaAPI {

    private final UserRepository userRepository;

    private final AuthTokenCacheService authTokenCacheService;

    private final TempTokenCacheService tempTokenCacheService;

    private final EsiaConfig esiaConfig;

    public EsiaAPI(UserRepository userRepository, AuthTokenCacheService authTokenCacheService, TempTokenCacheService tempTokenCacheService, EsiaConfig esiaConfig) {
        this.userRepository = userRepository;
        this.authTokenCacheService = authTokenCacheService;
        this.tempTokenCacheService = tempTokenCacheService;
        this.esiaConfig = esiaConfig;
    }

    /**
     * View для перенаправления на авторизацию в esia.
     * Если пользователь уже авторизован с системе - его перенаправляет
     * на страницу ESIA_REDIRECT_AFTER_LOGIN.
     * @param referer
     * @return redirect
     * @throws IOException
     */
    @Transactional
    @GetMapping("/oauth-link")
    public ResponseEntity<String> getAuthLink(@RequestHeader(required = false) String referer) throws
            CertificateException, InvalidKeySpecException, NoSuchAlgorithmException, OperatorCreationException,
            NoSuchProviderException, CMSException, IOException {

        final String redirectUrl;
        User user = null;
        if (user == null || true /*user.is_anonymous()*/) {
            final EsiaAuth esia_auth = get_esia_auth(referer);
            redirectUrl = esia_auth.get_auth_url(null, null); //"/?temp_token=123";
        } else {
            redirectUrl = esiaConfig.getRedirectAfterLoginUrl();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state
    ) throws IOException, CertificateException, InvalidKeySpecException, NoSuchAlgorithmException,
            OperatorCreationException, NoSuchProviderException, CMSException {

        final ZoneId zoneId = ZoneId.systemDefault(); // TODO get from properties
        final LocalDateTime nowTime = LocalDateTime.now(zoneId);

        if (code == null || state == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden options...");
        }

        final EsiaAuth esia_auth = get_esia_auth(null);

        // TODO: починить валидацию токена (нужно прикрутить ключ сервера ЕСИА)
        EsiaInformationConnector esiaInformationConnector;
        try {
            esiaInformationConnector = esia_auth.complete_authorization(code, state, false);
        } catch (SocketException ex) {
            log.warn("esia_auth.complete_authorization SocketException");
            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "Request timeout");
        }

        final Map<String, Object> person_info = esiaInformationConnector.get_person_main_info(null);
        final Map<String, Object> person_addresses = esiaInformationConnector.get_person_addresses(null);
        final Map<String, Object> person_contacts = esiaInformationConnector.get_person_contacts(null);
        final Map<String, Object> person_documents = esiaInformationConnector.get_person_documents(null);
        final Map<String, Object> person_orgs = esiaInformationConnector.get_person_orgs(null);

        final long oid = esiaInformationConnector.getOid();
        final String firstName = (String) person_info.get("firstName");
        final String lastName = (String) person_info.getOrDefault("lastName", "");
        final String middleName = (String) person_info.getOrDefault("middleName", "");

        // TODO refactor this
        final String email = (String)((List)person_contacts.getOrDefault("elements", new ArrayList<>())).stream()
                .filter(m -> ((Map)m).getOrDefault("type", "").equals("EML"))
                .map(m -> ((Map)m).getOrDefault("value", ""))
                .findFirst().orElse("");

        final String phoneNumber = ((String)((List)person_contacts.getOrDefault("elements", new ArrayList<>())).stream()
                .filter(m -> ((Map)m).getOrDefault("type", "").equals("MBT"))
                .map(m -> ((Map)m).getOrDefault("value", ""))
                .findFirst().orElse(""))
                .replace("(", "").replace(")", "");

        final String passportData = (String)((List)person_documents.getOrDefault("elements", new ArrayList<>())).stream()
                .filter(m -> ((Map)m).getOrDefault("type", "").equals("RF_PASSPORT"))
                .map(m -> ((Map)m).getOrDefault("series", "") + " " + ((Map)m).getOrDefault("number", ""))
                .findFirst()
                .orElse("");

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
            newUser.getRoles().add(UserRole.USER);
            newUser.setCreateDateTime(nowTime);

            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setPatronymicName(middleName);
            newUser.setPhone(phoneNumber);
            newUser.setPassport(passportData);
            newUser.setEmail(email);
            return userRepository.save(newUser);
        });

        final Optional<AuthTokenCache> optionalAuthToken = authTokenCacheService.findByUser(user);

        final AuthTokenCache authTokenCache = optionalAuthToken.orElseGet(() -> {
            return authTokenCacheService.createForUser(user, zoneId);
        });

        final TempTokenCache tempTokenCache = tempTokenCacheService.createFor(authTokenCache);

        final String redirect_url = String.format("%s?temp_token=%s",
                esiaConfig.getRedirectAfterLoginUrl(),
                URLEncoder.encode(tempTokenCache.getId(), StandardCharsets.UTF_8.name())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirect_url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        HttpHeaders headers = new HttpHeaders();
        final String redirect_url = esiaConfig.getRedirectAfterLoginUrl();
        headers.setLocation(URI.create(redirect_url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }



    protected EsiaAuth get_esia_auth(String redirect_after_login) throws UnsupportedEncodingException {
        String redirect_uri = esiaConfig.getRedirectUrl();

        if (redirect_after_login != null) {
            if (redirect_uri.contains("?")) {
                redirect_uri = String.format("%s&origin=%s", redirect_uri, URLEncoder.encode(redirect_after_login, StandardCharsets.UTF_8.name()));
            } else {
                redirect_uri = String.format("%s?origin=%s", redirect_uri, URLEncoder.encode(redirect_after_login, StandardCharsets.UTF_8.name()));
            }
        }

        final EsiaProperties newEsiaProperties = new EsiaProperties();
        newEsiaProperties.setClientId(esiaConfig.getClientId());
        newEsiaProperties.setKeysDir(esiaConfig.getKeysDir());
        newEsiaProperties.setCertificate(esiaConfig.getCertificate());
        newEsiaProperties.setPrivateKey(esiaConfig.getPrivateKey());
        newEsiaProperties.setTokenCheckKey(esiaConfig.getTokenCheckKey());
        newEsiaProperties.setRedirectUrl(redirect_uri);
        newEsiaProperties.setRedirectAfterLoginUrl(esiaConfig.getRedirectAfterLoginUrl());
        newEsiaProperties.setServiceUrl(esiaConfig.getServiceUrl());
        newEsiaProperties.setScopes(esiaConfig.getScopes());
        return new EsiaAuth(newEsiaProperties);
    }

}
