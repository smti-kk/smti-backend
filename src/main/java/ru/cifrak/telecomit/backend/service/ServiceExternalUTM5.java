package ru.cifrak.telecomit.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;
import ru.cifrak.telecomit.backend.security.UTM5Config;
import ru.cifrak.telecomit.backend.utils.IpReversed;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ServiceExternalUTM5 {
    private final UTM5Config utm5Config;

    public ServiceExternalUTM5(UTM5Config utm5Config) {
        this.utm5Config = utm5Config;
    }

    public void linking(AccessPoint ap, MonitoringAccessPoint map) throws JsonProcessingException {
         log.info("[ ->] insert into UTM5");
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

        // STEP-2: CREATE NEW USER IN UTM5
        ObjectNode jsonParams = jsonMapper.createObjectNode();
        jsonParams.put("login", "telecomit-" + ap.getId());
        jsonParams.put("password", utm5Config.getUserpwd());
        if (ap.getOrganization().getAcronym() != null && !ap.getOrganization().getAcronym().isEmpty()) {
            jsonParams.put("full_name", ap.getOrganization().getAcronym() + " (" + ap.getAddress() + ")");
        } else {
            jsonParams.put("full_name", ap.getOrganization().getId() + " (" + ap.getAddress() + ")");
        }
        jsonParams.put("is_juridical", false);
        jsonParams.put("juridical_address", "");
        jsonParams.put("actual_address", "");
        jsonParams.put("flat_number", "");
        jsonParams.put("entrance", "");
        jsonParams.put("floor", "");
        jsonParams.put("district", "");
        jsonParams.put("building", "");
        jsonParams.put("passport", "");
        jsonParams.put("house_id", 0);
        jsonParams.put("work_telephone", "");
        jsonParams.put("home_telephone", "");
        jsonParams.put("mobile_telephone", "");
        jsonParams.put("web_page", "");
        jsonParams.put("icq_number", "");
        jsonParams.put("tax_number", "");
        jsonParams.put("kpp_number", "");
        jsonParams.put("email", "");
        jsonParams.put("bank_id", 0);
        jsonParams.put("bank_account", "");
        jsonParams.put("comments", "");
        jsonParams.put("personal_manager", "");
        jsonParams.put("connect_date", 0);
        jsonParams.put("is_send_invoice", false);
        jsonParams.put("advance_payment", 0);
        jsonParams.put("router_id", 0);
        jsonParams.put("port_number", 0);
        jsonParams.put("binded_currency_id", 0);
        ArrayNode jsonParamsAdditionalArray = jsonMapper.createArrayNode();
        jsonParams.set("additional_params", jsonParamsAdditionalArray);
        ArrayNode jsonParamsGroupsArray = jsonMapper.createArrayNode();
        jsonParams.set("groups", jsonParamsGroupsArray);
        jsonParams.put("is_blocked", false);
        jsonParams.put("balance", 0);
        jsonParams.put("credit", 0);
        jsonParams.put("vat_rate", 0);
        jsonParams.put("sale_tax_rate", 0);
        jsonParams.put("int_status", 0);
        log.debug("{}", jsonParams);
        WebClient.RequestHeadersSpec<?> apiCreateClient = client
                .post()
                .uri("/api/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "session_id undefined")
                .header("Cookie", "session_id=" + sessionId)
                .body(BodyInserters.fromValue(jsonParams));
        //TODO:[generate TICKET]: needed error handling
        String jsonResponseCreateClient = apiCreateClient.retrieve().bodyToMono(String.class).block();
        Map<String, Integer> mapNewUserCredential = jsonMapper.readValue(jsonResponseCreateClient, Map.class);
        map.setIdUser(mapNewUserCredential.get("user_id"));
        map.setIdAccount(mapNewUserCredential.get("account_id"));

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
                .header("Cookie", "session_id=" + sessionId)
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
                .header("Cookie", "session_id=" + sessionId)
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

        // STEP-LAST: END
        log.info("[ <-] insert into UTM5");
    }
}
