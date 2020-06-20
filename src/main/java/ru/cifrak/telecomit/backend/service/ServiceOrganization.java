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
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
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
    private final UTM5Config utm5Config;
    private final ZabbixConfig zabbixConfig;

    public ServiceOrganization(RepositoryOrganization rOrganization, RepositoryAccessPoints rAccessPoints, UTM5Config utm5Config, ZabbixConfig zabbixConfig) {
        this.rOrganization = rOrganization;
        this.rAccessPoints = rAccessPoints;
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
        if (ap.getOrganization().getId().equals(id)) {
            //TODO: wrap with try-catch
            insertIntoUTM5(ap);
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
                Map<String, String> map = mapper.readValue(resp, Map.class);

                log.info("[   ] authenticated: {}", map.get("result"));
                log.info("[   ] go for create user");
                insertIntoZabbix(client, map.get("result"), wizard.getDevice());
                insertIntoZabbix(client, map.get("result"), wizard.getSensor());
            } catch (Exception e) {
                throw e;
            }
            //TODO: some here we should save monitoring data IDs and so on.`
            return "Access Point has bean initialized in monitorings.";
        } else {
            throw new Exception("You cannot init Access Point in non belonging Organization");
        }
    }

    //TODO: implement fully for ticket #473
    private void insertIntoUTM5(AccessPoint ap) {
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
        log.info("[   ] auth account: {}", auth);
        WebClient.RequestHeadersSpec<?> authenticate = client
                .post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(auth));
        String resp = authenticate.retrieve().bodyToMono(String.class).block();
        log.info("[   ] authenticated: {}", resp);
        log.info("[   ] go for create user");
        log.info("[ <-] insert into UTM5");
    }

    //TODO: ticket #475
    private void insertIntoZabbix(WebClient client, String authToken, ZabbixDTO zabbix) throws JsonProcessingException {
        log.info("[ ->] insert into ZABBIX");
        createHost(client, authToken,
                zabbix.getHostName(),
                zabbix.getIp(),
                zabbix.getGroupid(),
                zabbix.getTag(),
                zabbix.getTagValue(),
                zabbix.getTemplateid(),
                zabbix.getMacro(),
                zabbix.getMacroValue()
        );
        log.info("[ <-] insert into ZABBIX");
    }

    private String createHost(final WebClient client, String authToken,
                              String hostName,
                              String ip,
                              String groupid,
                              String tag, String tagValue,
                              String templateid,
                              String macro, String macroValue
    ) throws JsonProcessingException {
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
                .body(BodyInserters.fromValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonRPC)));
        String rpcResponce = requestNewHost.retrieve().bodyToMono(String.class).block();
        log.info("[   ] [   ]:responce: {} ", rpcResponce);
        return rpcResponce;
    }
}
