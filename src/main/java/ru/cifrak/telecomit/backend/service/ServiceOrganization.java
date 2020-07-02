package ru.cifrak.telecomit.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.cifrak.telecomit.backend.api.dto.MonitoringAccessPointWizardDTO;
import ru.cifrak.telecomit.backend.api.dto.OrganizationDTO;
import ru.cifrak.telecomit.backend.api.dto.ZabbixDTO;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryJournalMAP;
import ru.cifrak.telecomit.backend.repository.RepositoryMonitoringAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;
import ru.cifrak.telecomit.backend.security.UTM5Config;
import ru.cifrak.telecomit.backend.security.ZabbixConfig;
import ru.cifrak.telecomit.backend.utils.IpReversed;

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
    private final RepositoryMonitoringAccessPoints rMonitoringAccessPoints;
    private final RepositoryJournalMAP rJournalMAP;
    private final UTM5Config utm5Config;
    private final ZabbixConfig zabbixConfig;

    public ServiceOrganization(RepositoryOrganization rOrganization, RepositoryAccessPoints rAccessPoints, RepositoryMonitoringAccessPoints rMonitoringAccessPoints, RepositoryJournalMAP rJournalMAP, UTM5Config utm5Config, ZabbixConfig zabbixConfig) {
        this.rOrganization = rOrganization;
        this.rAccessPoints = rAccessPoints;
        this.rMonitoringAccessPoints = rMonitoringAccessPoints;
        this.rJournalMAP = rJournalMAP;
        this.utm5Config = utm5Config;
        this.zabbixConfig = zabbixConfig;
    }


    public List<Organization> all() {
        return rOrganization.findAll();
    }

    public OrganizationDTO getOrganizationById(Integer id) {
        return rOrganization.findById(id).map(OrganizationDTO::new).orElse(null);
    }


    public String initializeMonitoringOnAp(Integer id, Integer apid, MonitoringAccessPointWizardDTO wizard) throws Exception {
        AccessPoint ap = rAccessPoints.getOne(apid);
        MonitoringAccessPoint map = new MonitoringAccessPoint();
        JournalMAP jmap = new JournalMAP();
        jmap.setAp(ap);
        jmap.setActive(Boolean.TRUE);
        if (ap.getOrganization().getId().equals(id)) {
            try {
                insertIntoUTM5(ap, map);
            } catch (Exception e) {
                throw e;
            }
            try {
                WebClient client = WebClient
                        .builder()
                        .baseUrl(zabbixConfig.getHost())
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", zabbixConfig.getHost()))
                        .build();

                ObjectMapper mapper = new ObjectMapper();

                ObjectNode jsonRPCParams = mapper.createObjectNode();
                jsonRPCParams.put("user", zabbixConfig.getLogin());
                jsonRPCParams.put("password", zabbixConfig.getPassword());

                ObjectNode jsonRPC = mapper.createObjectNode();
                jsonRPC.put("jsonrpc", "2.0");
                jsonRPC.put("method", "user.login");
                jsonRPC.set("params", jsonRPCParams);
                jsonRPC.put("id", 100);
                jsonRPC.set("auth", null);

                WebClient.RequestHeadersSpec<?> authenticate = client
                        .post()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonRPC)));

                String resp = authenticate.retrieve().bodyToMono(String.class).block();
                Map<String, String> respAuthentication = mapper.readValue(resp, Map.class);

                log.info("[   ] authenticated: {}", respAuthentication.get("result"));
                log.info("[   ] go for create user");
                String device = insertIntoZabbix(client, respAuthentication.get("result"), wizard.getDevice());
                String sensor = insertIntoZabbix(client, respAuthentication.get("result"), wizard.getSensor());
                map.setIdDevice(device);
                map.setIdSensor(sensor);
                log.debug("map: {}", map);
                rMonitoringAccessPoints.save(map);
                jmap.setMap(map);
                rJournalMAP.save(jmap);
            } catch (Exception e) {
                throw e;
            }
            return "Access Point has bean initialized in monitorings.";
        } else {
            throw new Exception("You cannot init Access Point in non belonging Organization");
        }
    }

    //TODO:[generate TICKET]: possible to use DTO instead of manual construct
    private void insertIntoUTM5(AccessPoint ap, MonitoringAccessPoint map) throws JsonProcessingException {
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
        log.debug("[   ] authenticated: {}", jsonResponseAuthentication);
        log.info("[   ] go for create user");

        // STEP-2: CREATE NEW USER IN UTM5
        ObjectMapper jsonMapper = new ObjectMapper();
        Map<String, String> mapAuthorization = jsonMapper.readValue(jsonResponseAuthentication, Map.class);
        String sessionId = mapAuthorization.get("session_id");
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
        jsonNewTariffLink.put("accounting_period_id", 14);
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
        ObjectNode ipItem = jsonMapper.createObjectNode();
        ipItem.put("ip", new IpReversed(ap.getIpConfig()).ip());
        ipItem.put("mask", Integer.valueOf(new IpReversed(ap.getIpConfig()).mask()));
        ipItem.put("mac", "");
        ipItem.put("login", "");
        ipItem.put("allowed_cid", "");
        ipItem.put("password", "");
        ipItem.put("pool_name", "");
        ipItem.put("flags", 0);
        ipItem.put("nfprovider_id", 0);
        ipItem.put("switch_id", 0);
        ipItem.put("port_id", 0);
        ipItem.put("vlan_id", 0);
        ipItem.put("pool_id", 0);
        ipItem.put("owner_id", 0);
        ipItem.put("owner_type", 0);
        ipItem.set("dhcp_options", jsonMapper.createArrayNode());
        ipItem.set("isg_attrs", jsonMapper.createArrayNode());
        jsonNewServiceLinkIPGroup.add(ipItem);
        // IP nodes by AP-networks
        if (ap.getNetworks() != null && !ap.getNetworks().isEmpty()) {
            String[] networks = ap.getNetworks().split("; ");
            for (String network : networks) {
                ObjectNode networkItem = jsonMapper.createObjectNode();
                networkItem.put("ip", new IpReversed(network).ip());
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

    private String insertIntoZabbix(WebClient client, String authToken, @NotNull ZabbixDTO zabbix) throws Exception {
        log.info("[ <>] insert into ZABBIX");
        return createHostAndGiveIdentification(client, authToken,
                zabbix.getHostName(),
                zabbix.getIp(),
                zabbix.getGroupid(),
                zabbix.getTag(),
                zabbix.getTagValue(),
                zabbix.getTemplateid(),
                zabbix.getMacro(),
                zabbix.getMacroValue()
        );

    }

    private String createHostAndGiveIdentification(final @NotNull WebClient client, String authToken,
                                                   String hostName,
                                                   String ip,
                                                   String groupid,
                                                   String tag, String tagValue,
                                                   String templateid,
                                                   String macro, String macroValue
    ) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode jsonRPCParams = mapper.createObjectNode();
        jsonRPCParams.put("host", hostName);

        // Interfaces
        ArrayNode jsonRPCParamsInterfacesArray = mapper.createArrayNode();
        ObjectNode jsonRPCParamsInterface = mapper.createObjectNode();
        jsonRPCParamsInterface.put("type", 2);
        jsonRPCParamsInterface.put("main", 1);
        jsonRPCParamsInterface.put("useip", 1);
        jsonRPCParamsInterface.put("ip", ip);
        jsonRPCParamsInterface.put("dns", "");
        jsonRPCParamsInterface.put("port", "161");
        jsonRPCParamsInterfacesArray.add(jsonRPCParamsInterface);
        jsonRPCParams.set("interfaces", jsonRPCParamsInterfacesArray);
        // Groups
        ArrayNode jsonRPCParamsGroupsArray = mapper.createArrayNode();
        ObjectNode jsonRPCParamsGroup = mapper.createObjectNode();
        jsonRPCParamsGroup.put("groupid", groupid);
        jsonRPCParamsGroupsArray.add(jsonRPCParamsGroup);
        jsonRPCParams.set("groups", jsonRPCParamsGroupsArray);

        // Tags
        ArrayNode jsonRPCParamsTagsArray = mapper.createArrayNode();
        ObjectNode jsonRPCParamsTag = mapper.createObjectNode();
        jsonRPCParamsTag.put("tag", tag);
        jsonRPCParamsTag.put("value", tagValue);
        jsonRPCParamsTagsArray.add(jsonRPCParamsTag);
        jsonRPCParams.set("tags", jsonRPCParamsTagsArray);

        // Templates
        ArrayNode jsonRPCParamsTemplatesArray = mapper.createArrayNode();
        ObjectNode jsonRPCParamsTemplate = mapper.createObjectNode();
        jsonRPCParamsTemplate.put("templateid", templateid);
        jsonRPCParamsTemplatesArray.add(jsonRPCParamsTemplate);
        jsonRPCParams.set("templates", jsonRPCParamsTemplatesArray);

        // Macros
        ArrayNode jsonRPCParamsMacrosArray = mapper.createArrayNode();
        ObjectNode jsonRPCParamsMacro = mapper.createObjectNode();
        jsonRPCParamsMacro.put("macro", macro);
        jsonRPCParamsMacro.put("value", macroValue);
        jsonRPCParamsMacrosArray.add(jsonRPCParamsMacro);

        jsonRPCParams.set("macros", jsonRPCParamsMacrosArray);

        ObjectNode jsonRPC = mapper.createObjectNode();
        jsonRPC.put("jsonrpc", "2.0");
        jsonRPC.put("method", "host.create");
        jsonRPC.set("params", jsonRPCParams);
        jsonRPC.put("id", 101);
        jsonRPC.put("auth", authToken);
        WebClient.RequestHeadersSpec<?> requestNewHost = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonRPC));
        String rpcResponce = requestNewHost.retrieve().bodyToMono(String.class).block();
        Map<String, Object> map = mapper.readValue(rpcResponce, Map.class);
        Map<String, List<String>> respResult;
        respResult = (Map<String, List<String>>) map.get("result");
        if (respResult != null) {
            List<String> respHostIds = respResult.get("hostids");
            String respHostId = respHostIds.get(0);
            log.info("[   ] [   ]:responce: {} ", respHostId);
            return respHostId;
        } else {
            //TODO:[generate TICKET]: make ZabbixNameExistsException
            // and make round to try get ID from this name.
            throw new Exception("Such name exists");
        }
    }
}
