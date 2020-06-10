package ru.cifrak.telecomit.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.cifrak.telecomit.backend.api.dto.OrganizationDTO;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;
import ru.cifrak.telecomit.backend.security.UTM5Config;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j

@Transactional
@Service
public class ServiceOrganization {
    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints rAccessPoints;
    private final UTM5Config utm5Config;


    public ServiceOrganization(RepositoryOrganization rOrganization, RepositoryAccessPoints rAccessPoints, UTM5Config utm5Config) {
        this.rOrganization = rOrganization;
        this.rAccessPoints = rAccessPoints;
        this.utm5Config = utm5Config;
    }


    public List<Organization> all() {
        return rOrganization.findAll();
    }

    public OrganizationDTO getOrganizationById(Integer id) {
        return rOrganization.findById(id).map(OrganizationDTO::new).orElse(null);
    }


    public String initializeMonitoringOnAp(Integer id, Integer apid) throws Exception {
        AccessPoint ap = rAccessPoints.getOne(apid);
        if (ap.getOrganization().getId().equals(id)) {
            WebClient client = WebClient
                    .builder()
                    .baseUrl(utm5Config.getHost())
//                        .defaultCookie("cookieKey", "cookieValue")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", utm5Config.getHost()))
                    .build();

            Map<String, String> auth = new HashMap<>();
            auth.put("username", utm5Config.getLogin());
            auth.put("password", utm5Config.getPassword());
            log.info("auth account: {}", auth);
            WebClient.RequestHeadersSpec<?> authenticate = client
                    .post()
                    .uri("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(auth));
            String resp = authenticate.retrieve().bodyToMono(String.class).block();
            log.info("authenticated: {}", resp);
            log.info("go for create user");

            return "Hokey Dokey!!";
        } else {
            throw new Exception("You cannot init Access Point in non belonging Organization");
        }
    }
}
