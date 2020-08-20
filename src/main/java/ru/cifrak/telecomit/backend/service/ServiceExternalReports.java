package ru.cifrak.telecomit.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.cifrak.telecomit.backend.api.dto.ReportMapDTO;
import ru.cifrak.telecomit.backend.api.dto.UTM5ReportTrafficDTO;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryJournalMAP;
import ru.cifrak.telecomit.backend.repository.RepositoryMonitoringAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;
import ru.cifrak.telecomit.backend.security.UTM5Config;
import ru.cifrak.telecomit.backend.security.ZabbixConfig;
import ru.cifrak.telecomit.backend.utils.Converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j

@Transactional
@Service
public class ServiceExternalReports {
    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints rAccessPoints;
    private final RepositoryMonitoringAccessPoints rMonitoringAccessPoints;
    private final RepositoryJournalMAP rJournalMAP;
    private final UTM5Config utm5Config;
    private final ZabbixConfig zabbixConfig;

    public ServiceExternalReports(RepositoryOrganization rOrganization, RepositoryAccessPoints rAccessPoints, RepositoryMonitoringAccessPoints rMonitoringAccessPoints, RepositoryJournalMAP rJournalMAP, UTM5Config utm5Config, ZabbixConfig zabbixConfig) {
        this.rOrganization = rOrganization;
        this.rAccessPoints = rAccessPoints;
        this.rMonitoringAccessPoints = rMonitoringAccessPoints;
        this.rJournalMAP = rJournalMAP;
        this.utm5Config = utm5Config;
        this.zabbixConfig = zabbixConfig;
    }

    public List<UTM5ReportTrafficDTO> getReportFromUTM5(Long start, Long end) throws JsonProcessingException {
        log.info("[ ->] getReportFromUTM5");
        WebClient client = WebClient
                .builder()
                .baseUrl(utm5Config.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", utm5Config.getHost()))
                .build();
        // STEP-1: AUTHORIZATION
        Map<String, String> jsonRequestAuthorization = new HashMap<>();
        jsonRequestAuthorization.put("username", utm5Config.getLogin());
        jsonRequestAuthorization.put("password", utm5Config.getPassword());
        WebClient.RequestHeadersSpec<?> apiAuthenticate = client
                .post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonRequestAuthorization));
        String jsonResponseAuthentication = apiAuthenticate.retrieve().bodyToMono(String.class).block();
        ObjectMapper jsonMapper = new ObjectMapper();
        Map<String, String> mapAuthorization = jsonMapper.readValue(jsonResponseAuthentication, Map.class);
        String sessionId = mapAuthorization.get("session_id");

        // STEP-2: REQUEST FOR REPORT
        String reportResponce = client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/reports/traffic")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("user_id", 0)
                        .queryParam("account_id", 0)
                        .queryParam("accounting_period_id", 0)
                        .queryParam("group_id", 0)
                        .queryParam("type", 0)
                        .build()
                )
                .header("Authorization", "session_id undefined")
                .header("Cookie", "session_id=" + sessionId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class).block();
        log.debug("    :: response: {}", reportResponce);

        List<UTM5ReportTrafficDTO> mapReportResponce = jsonMapper.readValue(reportResponce, new TypeReference<List<UTM5ReportTrafficDTO>>() {
        });
        mapReportResponce.stream().forEach(i -> log.debug("    :: :: item: {}", i));

        log.info("[ <-] getReportFromUTM5");
        return mapReportResponce;
    }

    public List<ReportMapDTO> blendData(List<UTM5ReportTrafficDTO> dataUtm5) {
        Map<Integer, UTM5ReportTrafficDTO> utm5Data = dataUtm5.stream().filter(i->i.getTclass().equals(10)).collect(Collectors.toMap(UTM5ReportTrafficDTO::getAccount_id, item -> item));

        List<Integer> accounts = dataUtm5.stream().map(UTM5ReportTrafficDTO::getAccount_id).distinct().collect(Collectors.toList());

        List<JournalMAP> jmaps = rJournalMAP.findAllByMap_IdAccountIn(accounts);

        return jmaps.stream()
                .map(jmap -> {
                            ReportMapDTO item = new ReportMapDTO();
                            item.setAddress(jmap.getAp().getAddress());
                            item.setParent(jmap.getAp().getOrganization().getLocation().getParent().getName());
                            item.setConsumption(Converter.megabytes(utm5Data.get(jmap.getMap().getIdAccount()).getBytes()));
                            item.setContractor(jmap.getAp().getContractor());
                            item.setLocation(jmap.getAp().getOrganization().getLocation().getName());
                            item.setUcn(jmap.getAp().getUcn());
                            item.setOrganization(jmap.getAp().getOrganization().getName());
                            return item;
                        }

                ).collect(Collectors.toList());
    }
}
