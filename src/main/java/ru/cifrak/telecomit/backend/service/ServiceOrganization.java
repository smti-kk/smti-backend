package ru.cifrak.telecomit.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
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
            //TODO: ticket #473
            try {
                insertIntoUTM5(ap, wizard.getUtm5().getName());
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
                map.setDevice(device);
                map.setSensor(sensor);
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

    //TODO: ticket #473
    private void insertIntoUTM5(AccessPoint ap, String userLogin) throws JsonProcessingException {
        log.info("[ ->] insert into UTM5");
        WebClient client = WebClient
                .builder()
                .baseUrl(utm5Config.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", utm5Config.getHost()))
                .build();

        Map<String, String> auth = new HashMap<>();
        auth.put("username", utm5Config.getLogin());
        auth.put("password", utm5Config.getPassword());
        WebClient.RequestHeadersSpec<?> authenticate = client
                .post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(auth));
        String resp = authenticate.retrieve().bodyToMono(String.class).block();

        log.info("[   ] go for create user");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(resp, Map.class);
        String authToken = map.get("session_id");
        ObjectNode jsonParams = mapper.createObjectNode();
        jsonParams.put("login", userLogin);
        jsonParams.put("password", utm5Config.getUserpwd());
        jsonParams.put("full_name", "foo-test-1");
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
        ArrayNode jsonParamsAdditionalArray = mapper.createArrayNode();
        jsonParams.set("additional_params", jsonParamsAdditionalArray);
        ArrayNode jsonParamsGroupsArray = mapper.createArrayNode();
//        jsonParamsGroupsArray.add(102);
        jsonParams.set("groups", jsonParamsGroupsArray);
        jsonParams.put("is_blocked", false);
        jsonParams.put("balance", 0);
        jsonParams.put("credit", 0);
        jsonParams.put("vat_rate", 0);
        jsonParams.put("sale_tax_rate", 0);
        jsonParams.put("int_status", 0);
        WebClient.RequestHeadersSpec<?> responceCreateNewClient = client
                .post()
                .uri("/api/users/")
                .contentType(MediaType.APPLICATION_JSON)
//                .cookie("session_id",authToken)
                .header("Authorization", "session_id undefined")
                .header("Cookie", "session_id="+authToken)
//                .body(BodyInserters.fromValue(jsonParams));
        .body(BodyInserters.fromValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonParams)));
//        log.info("[   ] resp-obj: {}",responceCreateNewClient.);
        String responceCreateNewClientResult = responceCreateNewClient.retrieve().bodyToMono(String.class).block();

        log.info("[   ] authenticated: {}", resp);
        log.info("[   ] go for create user");
        Map<String, Object> responce = mapper.readValue(responceCreateNewClientResult, Map.class);
        log.info("[ <-] insert into UTM5");
    }

    private String insertIntoZabbix(WebClient client, String authToken, ZabbixDTO zabbix) throws Exception {
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

    private String createHostAndGiveIdentification (final WebClient client, String authToken,
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
        if (respResult !=null) {
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
