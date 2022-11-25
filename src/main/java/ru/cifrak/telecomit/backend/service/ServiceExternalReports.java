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
import ru.cifrak.telecomit.backend.api.dto.ZabbixReportDTO;
import ru.cifrak.telecomit.backend.api.dto.external.*;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryJournalMAP;
import ru.cifrak.telecomit.backend.repository.RepositoryMonitoringAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;
import ru.cifrak.telecomit.backend.security.UTM5Config;
import ru.cifrak.telecomit.backend.security.ZabbixConfig;
import ru.cifrak.telecomit.backend.utils.Converter;

import java.util.*;
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

    public List<ReportMapDTO> blendData(List<UTM5ReportTrafficDTO> dataUtm5, List<ZabbixReportDTO> dataZabbix) {
        Map<Integer, UTM5ReportTrafficDTO> utm5Data = dataUtm5.stream().filter(i->i.getTclass().equals(10)).collect(Collectors.toMap(UTM5ReportTrafficDTO::getAccount_id, item -> item));
        Map<Long, ZabbixReportDTO> zabbixData = dataZabbix.stream().collect(Collectors.toMap(ZabbixReportDTO::getServiceId, item -> item));

        List<JournalMAP> jmaps = rJournalMAP.findAll();

        return jmaps.stream()
                .map(jmap -> {
                            ReportMapDTO item = new ReportMapDTO();
                            item.setAddress(jmap.getAp().getAddress());
                            item.setParent(jmap.getAp().getOrganization().getLocation().getParent().getName());
                            if (utm5Data.get(jmap.getMap().getIdAccount()) != null) {
                                item.setConsumption(Converter.megabytes(utm5Data.get(jmap.getMap().getIdAccount()).getBytes()));
                            }
                            //item.setContractor(jmap.getAp().getContractor());
                            item.setLocation(jmap.getAp().getOrganization().getLocation().getType() + ". " + jmap.getAp().getOrganization().getLocation().getName());
                            item.setUcn(jmap.getAp().getUcn());
                            item.setOrganization(jmap.getAp().getOrganization().getName());
                            if (zabbixData.get(jmap.getMap().getServiceId())!=null) {
                                item.setSla(zabbixData.get(jmap.getMap().getServiceId()).getSla());
                                item.setProblemTime(zabbixData.get(jmap.getMap().getServiceId()).getProblemTime());
                            }
                            item.setZabbixDeviceIp(jmap.getMap().getDeviceIp());
                            item.setZabbixDeviceName(jmap.getMap().getDeviceName());
                            item.setInternetAccessType(jmap.getAp().getInternetAccess().getName());
                            item.setNetworks(jmap.getAp().getNetworks());
                            return item;
                        }

                ).collect(Collectors.toList());
    }

    public List<ZabbixReportDTO> getReportFromZabbix(Long start, Long end) throws JsonProcessingException {
        log.info("[ ->] getReportFromZabbix");
        WebClient client = WebClient
                .builder()
                .baseUrl(zabbixConfig.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", zabbixConfig.getHost()))
                .build();
        // STEP-1: AUTHORIZATION
        ObjectMapper mapper = new ObjectMapper();

        WebClient.RequestHeadersSpec<?> authenticate = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoAuth(zabbixConfig.getLogin(), zabbixConfig.getPassword())));

        String resp = authenticate.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponse respAuthentication = mapper.readValue(resp, ExtZabbixDtoResponse.class);

        String authToken = (String) respAuthentication.getResult();
        // STEP-2: REQUEST FOR REPORT
        log.info("[   ] -> get sla for devices");
        // Сходить в базу и найти все те точки которые мы можем собрать в отчет
        List<MonitoringAccessPoint> list = rMonitoringAccessPoints.findAll();
        List<MonitoringAccessPoint> maps = list.stream().filter(i -> i.getServiceId()!=null).collect(Collectors.toList());
        WebClient.RequestHeadersSpec<?> serviceSlaRequest = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoRequest("service.getsla", new ExtZabbixDtoGetServiceSlaParams(maps, start, end), 42, authToken)));
        String slaResponce = serviceSlaRequest.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseSla listSlaData = mapper.readValue(slaResponce, ExtZabbixDtoResponseSla.class);
        List<ZabbixReportDTO> bar = new ArrayList<>();
        for (Map.Entry<String, ExtZabbixDtoResponseSlaData> entry : listSlaData.getResult().entrySet()){
            ZabbixReportDTO item = new ZabbixReportDTO();
            item.setServiceId(Long.valueOf(entry.getKey()));
            item.setSla(entry.getValue().getSla().get(0).getSla().toString());
            item.setProblemTime(String.valueOf(entry.getValue().getSla().get(0).getProblemTime()/60));
            bar.add(item);
        }
        log.info("[   ] -> get sla for devices");

        log.info("[ <-] getReportFromZabbix");
        return bar;
    }
}
