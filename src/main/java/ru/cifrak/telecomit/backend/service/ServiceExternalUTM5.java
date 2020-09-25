package ru.cifrak.telecomit.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.cifrak.telecomit.backend.api.dto.external.utm5.ExtUtmDtoAuth;
import ru.cifrak.telecomit.backend.api.dto.external.utm5.ExtUtmDtoRequestCreateUser;
import ru.cifrak.telecomit.backend.api.dto.external.utm5.ExtUtmDtoResponseAuth;
import ru.cifrak.telecomit.backend.api.dto.external.utm5.ExtUtmDtoResponseUser;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;
import ru.cifrak.telecomit.backend.security.UTM5Config;
import ru.cifrak.telecomit.backend.utils.IpReversed;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ServiceExternalUTM5 {
    private final UTM5Config utm5Config;
    private WebClient client;
    private ObjectMapper jsonMapper = new ObjectMapper();

    public ServiceExternalUTM5(UTM5Config utm5Config) {
        this.utm5Config = utm5Config;
        // Интересно правильно ли я сюда запихал? может надо было @PostConstruct ?
        client = WebClient
                .builder()
                .baseUrl(utm5Config.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", utm5Config.getHost()))
                .build();

    }

    public void linking(AccessPoint ap, MonitoringAccessPoint map) throws JsonProcessingException {
        log.info("[ ->] insert into UTM5");
        // xx. AUTHORIZATION
        ExtUtmDtoResponseAuth respAuthorization = authorize();
        // xx. ACCESS POINT => USER
        // xx.xx. CHECKOUT IF AP ALREADY EXISTS
        List<ExtUtmDtoResponseUser> users = getUsers(respAuthorization);
        ExtUtmDtoResponseUser user = users.stream().filter(i -> i.getLogin().equals("telecomit-" + ap.getId())).findFirst().orElse(null);
        if (user != null) {
            // xx.xx. UPDATE OUR
            log.info("find one: {}", user);
            updateMonitoringAccessPointWithUserData(map, user);
            updateMonitoringAccessPointWithTariff(respAuthorization, map);
        } else {
            // xx.xx. CREATE NEW ONE
            createUser(respAuthorization, ap, map);
            log.info("go for create");

            // STEP-3: CREATE TARIFF LINK TO USER
            ObjectNode jsonNewTariffLink = jsonMapper.createObjectNode();
            jsonNewTariffLink.put("user_id", map.getIdUser());
            jsonNewTariffLink.put("account_id", map.getIdAccount());
            //TODO:[generate TICKET]: this value should choose from system
            jsonNewTariffLink.put("first_tariff_id", 2);
            jsonNewTariffLink.put("second_tariff_id", 0);
            //TODO:[generate TICKET]: this value should choose from system
            jsonNewTariffLink.put("accounting_period_id", 16);
            jsonNewTariffLink.put("tariff_link_id", 0);
            jsonNewTariffLink.put("change_now", false);
            log.debug("{}", jsonNewTariffLink);
            WebClient.RequestHeadersSpec<?> apiCreateTraffic = client
                    .post()
                    .uri("/api/users/tarifflinks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "session_id undefined")
                    .header("Cookie", "session_id=" + respAuthorization.getSession_id())
                    .body(BodyInserters.fromValue(jsonNewTariffLink));
            //TODO:[generate TICKET]: needed error handling
            String jsonResponseCreateTraffic = apiCreateTraffic.retrieve().bodyToMono(String.class).block();
            Map<String, Integer> mapNewTrafficCredential = jsonMapper.readValue(jsonResponseCreateTraffic, Map.class);
            map.setIdTraffic(mapNewTrafficCredential.get("tariff_link_id"));

            // STEP-3: CREATE SERVICE LINK FOR USER
            ObjectNode jsonNewServiceLink = jsonMapper.createObjectNode();
            jsonNewServiceLink.put("user_id", map.getIdUser());
            jsonNewServiceLink.put("account_id", map.getIdAccount());
            //TODO:[generate TICKET]: this value should choose from system
            jsonNewServiceLink.put("service_id", 6);
            jsonNewServiceLink.put("tplink_id", map.getIdTraffic());
            //TODO:[generate TICKET]: this value should choose from system
            jsonNewServiceLink.put("accounting_period_id", 2);
            jsonNewServiceLink.put("start_date", 0);
            jsonNewServiceLink.put("expire_date", 0);
            jsonNewServiceLink.put("policy_id", 1);
            jsonNewServiceLink.put("cost_coef", 1);
            jsonNewServiceLink.put("house_comment", "");
            jsonNewServiceLink.put("comment", "");
            jsonNewServiceLink.put("unabon", 0);
            jsonNewServiceLink.put("house_id", 0);
            jsonNewServiceLink.put("unprepay", 0);
//        ip_group
            ArrayNode jsonNewServiceLinkIPGroup = jsonMapper.createArrayNode();
            // IP node by AP
            // HINT: Commented out due task 1045, we do not need traffic count for hardware ap itself. For now...
//        ObjectNode ipItem = jsonMapper.createObjectNode();
//        ipItem.put("ip", new IpReversed(ap.getIpConfig()).ip());
//        ipItem.put("mask", Integer.valueOf(new IpReversed(ap.getIpConfig()).mask()));
//        ipItem.put("mac", "");
//        ipItem.put("login", "");
//        ipItem.put("allowed_cid", "");
//        ipItem.put("password", "");
//        ipItem.put("pool_name", "");
//        ipItem.put("flags", 0);
//        ipItem.put("nfprovider_id", 0);
//        ipItem.put("switch_id", 0);
//        ipItem.put("port_id", 0);
//        ipItem.put("vlan_id", 0);
//        ipItem.put("pool_id", 0);
//        ipItem.put("owner_id", 0);
//        ipItem.put("owner_type", 0);
//        ipItem.set("dhcp_options", jsonMapper.createArrayNode());
//        ipItem.set("isg_attrs", jsonMapper.createArrayNode());
//        jsonNewServiceLinkIPGroup.add(ipItem);
            // IP nodes by AP-networks
            if (ap.getNetworks() != null && !ap.getNetworks().isEmpty()) {
                String[] networks = ap.getNetworks().split("; ");
                for (String network : networks) {
                    ObjectNode networkItem = jsonMapper.createObjectNode();
                    networkItem.put("ip", new IpReversed(network).ipStrait());
                    networkItem.put("mask", Integer.valueOf(new IpReversed(network).mask()));
                    networkItem.put("mac", "");
                    networkItem.put("login", "");
                    networkItem.put("allowed_cid", "");
                    networkItem.put("password", "");
                    networkItem.put("pool_name", "");
                    networkItem.put("flags", 0);
                    networkItem.put("nfprovider_id", 0);
                    networkItem.put("switch_id", 0);
                    networkItem.put("port_id", 0);
                    networkItem.put("vlan_id", 0);
                    networkItem.put("pool_id", 0);
                    networkItem.put("owner_id", 0);
                    networkItem.put("owner_type", 0);
                    networkItem.set("dhcp_options", jsonMapper.createArrayNode());
                    networkItem.set("isg_attrs", jsonMapper.createArrayNode());
                    jsonNewServiceLinkIPGroup.add(networkItem);
                }
            }

            jsonNewServiceLink.set("ip_group", jsonNewServiceLinkIPGroup);
//        quotas_count
            ArrayNode jsonNewServiceLinkQuotasCount = jsonMapper.createArrayNode();
            ObjectNode quotaItem = jsonMapper.createObjectNode();
            quotaItem.put("tclass", 10);
            quotaItem.put("traffic_quota", 100);
            jsonNewServiceLinkQuotasCount.add(quotaItem);
            jsonNewServiceLink.set("quotas_count", jsonNewServiceLinkQuotasCount);
            log.debug("{}", jsonNewServiceLink);
            WebClient.RequestHeadersSpec<?> apiCreateService = client
                    .post()
                    .uri("/api/users/servicelinks/iptraffic")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "session_id undefined")
                    .header("Cookie", "session_id=" + respAuthorization.getSession_id())
                    .body(BodyInserters.fromValue(jsonNewServiceLink));
            //TODO:[generate TICKET]: needed error handling
            String jsonResponseCreateService = null;
            try {
                jsonResponseCreateService = apiCreateService.retrieve().bodyToMono(String.class).block();
            } catch (WebClientResponseException e) {
                log.error("{}", e.getResponseBodyAsString());
            }
            Map<String, Integer> mapNewServiceCredential = jsonMapper.readValue(jsonResponseCreateService, Map.class);
            map.setIdService(mapNewServiceCredential.get("service_link_id"));
        }
        // STEP-LAST: END
        log.info("[ <-] insert into UTM5");
    }

    private void updateMonitoringAccessPointWithTariff(ExtUtmDtoResponseAuth respAuthorization, MonitoringAccessPoint map) throws JsonProcessingException {
        log.info("_>_ get tariff");
        WebClient.RequestHeadersSpec<?> apiAuthenticate = client
                .get()
                .uri(ub -> ub.path("/api/users/tarifflinks")
                        .queryParam("user_id", map.getIdUser())
                        .queryParam("account_id", map.getIdAccount())
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "session_id undefined")
                .header("Cookie", "session_id=" + respAuthorization.getSession_id());
        String response = apiAuthenticate.retrieve().bodyToMono(String.class).block();
        List<Map<String, Integer>> mapNewUserCredential = jsonMapper.readValue(response, new TypeReference<List<Map<String, Integer>>>() {
        });
        map.setIdTraffic(mapNewUserCredential.get(0).get("id"));
        log.info("_<_ get tariff");

    }

    private MonitoringAccessPoint updateMonitoringAccessPointWithUserData(MonitoringAccessPoint map, ExtUtmDtoResponseUser user) {
        log.info("_>_ updateMonitoringAccessPointWithUserData");
        map.setIdUser(Integer.valueOf(user.getUser_id()));
        map.setIdAccount(Integer.valueOf(user.getAccounts().get(0)));
        map.setIdService(user.getSlinks().get(0));
        log.info("_<_ updateMonitoringAccessPointWithUserData");
        return map;
    }

    private List<ExtUtmDtoResponseUser> getUsers(ExtUtmDtoResponseAuth respAuthorization) throws JsonProcessingException {
        log.info("_>_ getUsers");
        WebClient.RequestHeadersSpec<?> apiAuthenticate = client
                .get()
                .uri("/api/users/")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "session_id undefined")
                .header("Cookie", "session_id=" + respAuthorization.getSession_id());
        String rawResponse = apiAuthenticate.retrieve().bodyToMono(String.class).block();

        List<ExtUtmDtoResponseUser> response = jsonMapper.readValue(rawResponse, new TypeReference<List<ExtUtmDtoResponseUser>>() {
        });
        log.info("_<_ getUsers");
        return response;
    }

    private ExtUtmDtoResponseAuth authorize() throws JsonProcessingException {
        log.info("_>_ authorize");
        WebClient.RequestHeadersSpec<?> apiAuthenticate = client
                .post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtUtmDtoAuth(utm5Config.getLogin(), utm5Config.getPassword())));
        String jsonResponseAuthentication = apiAuthenticate.retrieve().bodyToMono(String.class).block();

        ExtUtmDtoResponseAuth respAuthorization = jsonMapper.readValue(jsonResponseAuthentication, ExtUtmDtoResponseAuth.class);
        log.info("_<_ authorize");
        return respAuthorization;
    }

    private void createUser(ExtUtmDtoResponseAuth respAuthorization, AccessPoint ap, MonitoringAccessPoint map) throws JsonProcessingException {
        log.info("_>_ create user");
        ExtUtmDtoRequestCreateUser newUser = new ExtUtmDtoRequestCreateUser(ap, utm5Config, respAuthorization.getSession_id());
        WebClient.RequestHeadersSpec<?> apiCreateClient = client
                .post()
                .uri("/api/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "session_id undefined")
                .header("Cookie", "session_id=" + respAuthorization.getSession_id())
                .body(BodyInserters.fromValue(newUser));
        String response = apiCreateClient.retrieve().bodyToMono(String.class).block();
        Map<String, Integer> mapNewUserCredential = jsonMapper.readValue(response, Map.class);
        map.setIdUser(mapNewUserCredential.get("user_id"));
        map.setIdAccount(mapNewUserCredential.get("account_id"));
        log.info("_<_ create user");
    }
}
