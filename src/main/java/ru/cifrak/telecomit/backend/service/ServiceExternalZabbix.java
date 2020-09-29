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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.cifrak.telecomit.backend.api.dto.MonitoringAccessPointWizardDTO;
import ru.cifrak.telecomit.backend.api.dto.external.*;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;
import ru.cifrak.telecomit.backend.security.ZabbixConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ServiceExternalZabbix {
    private final ZabbixConfig zabbixConfig;

    public ServiceExternalZabbix(ZabbixConfig zabbixConfig) {
        this.zabbixConfig = zabbixConfig;
    }

    public void linking(AccessPoint ap, MonitoringAccessPoint map, MonitoringAccessPointWizardDTO wizard) throws Exception {
        WebClient client = WebClient
                .builder()
                .baseUrl(zabbixConfig.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", zabbixConfig.getHost()))
                .build();

        ObjectMapper mapper = new ObjectMapper();

        WebClient.RequestHeadersSpec<?> authenticate = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoAuth(zabbixConfig.getLogin(), zabbixConfig.getPassword())));

        String resp = authenticate.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponse respAuthentication = mapper.readValue(resp, ExtZabbixDtoResponse.class);
        String authToken = (String) respAuthentication.getResult();

        // xx. где-то здесь по идее необходимо проверять что устройство уже заведенно в системе.
        log.info("(>) go 4 work");
        if (wizard.getDevice() != null) {
//         check if Zabbix already have device
            if (isExistsDevice(wizard.getDevice(), mapper, client, authToken)) {
                // UPDATE
                log.info("(>) work update device");
                ExtZabbixDtoResponseHost remoteItem = getDeviceData(wizard.getDevice(), mapper, client, authToken);
                log.warn("{}", remoteItem);
                map.setDeviceId(remoteItem.getResult().get(0).getHostid());
                map.setDeviceName(remoteItem.getResult().get(0).getHost());
                map.setDeviceIp(remoteItem.getResult().get(0).getInterfaces().get(0).getIp());
                map.setDeviceTriggerUnavailable(
                        Long.valueOf(
                                remoteItem.getResult().get(0).getTriggers()
                                        .stream()
                                        .filter(i -> i.getDescription().equals("Unavailable by ICMP ping"))
                                        .findFirst()
                                        .get()
                                        .getTriggerid()
                        )
                );
                map.setDeviceTriggerResponseLost(
                        Long.valueOf(
                                remoteItem.getResult().get(0).getTriggers()
                                        .stream()
                                        .filter(i -> i.getDescription().equals("High ICMP ping loss"))
                                        .findFirst()
                                        .get()
                                        .getTriggerid()
                        )
                );
                map.setDeviceTriggerResponseLow(
                        Long.valueOf(
                                remoteItem.getResult().get(0).getTriggers()
                                        .stream()
                                        .filter(i -> i.getDescription().equals("High ICMP ping response time"))
                                        .findFirst()
                                        .get()
                                        .getTriggerid()
                        )
                );
                log.info("(<) work update device");
            } else {
                // CREATE
                log.info("(><) work create device");
//                createDevice(map, wizard, client, authToken);

                // завести триггеры на устройство :: это у нас по доступности
            }
        }
        if (wizard.getSensor() != null) {
//         check if Zabbix already have sensor
//         createSensor(map, wizard, client, authToken);
            if (isExistsDevice(wizard.getSensor(), mapper, client, authToken)) {
                log.info("(>) work update sensor");
                ExtZabbixDtoResponseHost remoteItem = getDeviceData(wizard.getSensor(), mapper, client, authToken);
                log.warn("{}", remoteItem);
                map.setSensorId(remoteItem.getResult().get(0).getHostid());
                map.setSensorName(remoteItem.getResult().get(0).getHost());
                map.setSensorIp(remoteItem.getResult().get(0).getInterfaces().get(0).getIp());
                map.setSensorTriggerEnergy(
                        Long.valueOf(
                                remoteItem.getResult().get(0).getTriggers()
                                        .stream()
                                        .filter(i -> i.getDescription().equals("Unavailable by ICMP ping"))
                                        .findFirst()
                                        .get()
                                        .getTriggerid()
                        )
                );
                log.info("(<) work update sensor");
            } else {
                // завести триггеры на сенсор :: это у нас по электричеству
                log.info("(><) work create sensor");
            }
        }

/*

        // xx. Спросить тригеры которые получились для оборудования и для сенсора
        ExtZabbixDtoResponseTriggers listOfTriggersDevice = getExtZabbixDtoResponseTriggers(client, mapper, authToken, "[   ] -> get triggers for device", map.getDeviceId(), "[   ] <- get triggers for device");
        ExtZabbixDtoResponseTriggers listOfTriggersSensor = getExtZabbixDtoResponseTriggers(client, mapper, authToken, "[   ] -> get triggers for sensor", map.getSensorId(), "[   ] <- get triggers for sensor");

        // xx. Скорее всего сохранить эти тригеры в нашей БД (да. но если руками заббикс крутить, то эти данные могут протухнуть)
        // тут проинициализировать точки этими данными от тригерров

        // xx. Создать сервисы для девайса и для сенсора
        ExtZabbixDtoResponseService service = getExtZabbixDtoResponseService(ap, client, mapper, authToken);
        map.setServiceId(Long.valueOf(service.getResult().get(0)));

        // xx.xx. Услуга передача данных
        ExtZabbixDtoResponseService serviceData = getExtZabbixDtoResponseService(client, mapper, authToken, service, "[   ] -> go for create services data", "Уровень SLA по передаче данных", "[   ] <- go for create services data:: {}");

        // xx.xx.xx. Услуга High ICMP ping loss
        zzzPingLost(map, wizard, client, mapper, authToken, listOfTriggersDevice, serviceData);

        // xx.xx.xx. Услуга High ICMP ping response time
        zzzPingLow(map, wizard, client, mapper, authToken, listOfTriggersDevice, serviceData);

        // xx.xx.xx. Услуга Unavailable by ICMP ping
        zzzUnavailable(map, wizard, client, mapper, authToken, listOfTriggersDevice, serviceData);

        //----------- if sensor exists --------------
        // xx.xx. Услуга электросвязь
        ExtZabbixDtoResponseService serviceElectricity = getExtZabbixDtoResponseService(client, mapper, authToken, service, "[   ] -> go for create services electricity", "Доступность узла связи электроэнергия", "[   ] <- go for create services electricity:: {}");

        // xx.xx.xx. Услуга Unavailable by ICMP ping Energy
        zzzElectricity(map, wizard, client, mapper, authToken, listOfTriggersSensor, serviceElectricity);*/
    }

    private boolean isExistsDevice(ExtZabbixDto device, ObjectMapper mapper, WebClient client, String authToken) throws JsonProcessingException {
        WebClient.RequestHeadersSpec<?> request4Hosts = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        new ExtZabbixDtoRequest("host.get",
                                new ExtZabbixDtoGetHostsParams(device.getHostName()),
                                42,
                                authToken
                        )));
        String responseHosts = request4Hosts.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseHosts responseHostsData = mapper.readValue(responseHosts, ExtZabbixDtoResponseHosts.class);
        //TODO:[generate TICKET]: более описательную ошибку, ибо вдруг найдет больше. Так как поиск, по шаблону то происходит.
        return responseHostsData.getResult() == null ? false : responseHostsData.getResult().size() == 1;
    }

    private ExtZabbixDtoResponseHost getDeviceData(ExtZabbixDto device, ObjectMapper mapper, WebClient client, String authToken) throws JsonProcessingException {
        WebClient.RequestHeadersSpec<?> request4Hosts = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        new ExtZabbixDtoRequest("host.get",
                                new ExtZabbixDtoGetHostParams(device.getHostName()),
                                42,
                                authToken
                        )));
        String responseHosts = request4Hosts.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseHost responseHostData = mapper.readValue(responseHosts, ExtZabbixDtoResponseHost.class);
        return responseHostData;
    }

    private void zzzElectricity(MonitoringAccessPoint map, MonitoringAccessPointWizardDTO wizard, WebClient client, ObjectMapper mapper, String authToken, ExtZabbixDtoResponseTriggers listOfTriggersSensor, ExtZabbixDtoResponseService serviceElectricity) throws JsonProcessingException {
        log.info("[   ] -> go for create services data :Unavailable by ICMP ping Energy:");
        Long trgSE = Long.valueOf(listOfTriggersSensor.getResult().stream().filter(i -> i.getDescription().equals("Unavailable by ICMP ping")).findFirst().get().getTriggerid());
        map.setSensorTriggerEnergy(trgSE);
        WebClient.RequestHeadersSpec<?> xxxZE = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoRequest("service.create",
                        new ExtZabbixDtoCreateNewService(wizard.getSensor().getHostName() + ": Unavailable by ICMP ping Energy", Long.valueOf(serviceElectricity.getResult().get(0)), trgSE),
                        42,
                        authToken
                )));
        String responseServiceElectricityPing = xxxZE.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseService serviceElectricityPing = mapper.readValue(responseServiceElectricityPing, ExtZabbixDtoResponseService.class);
        log.info("[   ] <- go for create services data :Unavailable by ICMP ping Energy: :: {}", serviceElectricityPing);
    }

    private void zzzUnavailable(MonitoringAccessPoint map, MonitoringAccessPointWizardDTO wizard, WebClient client, ObjectMapper mapper, String authToken, ExtZabbixDtoResponseTriggers listOfTriggersDevice, ExtZabbixDtoResponseService serviceData) throws JsonProcessingException {
        log.info("[   ] -> go for create services data :Unavailable by ICMP ping:");
        Long trgUn = Long.valueOf(listOfTriggersDevice.getResult().stream().filter(i -> i.getDescription().equals("Unavailable by ICMP ping")).findFirst().get().getTriggerid());
        map.setDeviceTriggerUnavailable(trgUn);
        WebClient.RequestHeadersSpec<?> xxxYP = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoRequest("service.create",
                        new ExtZabbixDtoCreateNewService(wizard.getDevice().getHostName() + ": Unavailable by ICMP ping", Long.valueOf(serviceData.getResult().get(0)), trgUn),
                        42,
                        authToken
                )));
        String responseServiceDataPing = xxxYP.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseService serviceDataPing = mapper.readValue(responseServiceDataPing, ExtZabbixDtoResponseService.class);
        log.info("[   ] <- go for create services data :Unavailable by ICMP ping: :: {}", serviceDataPing);
    }

    private void zzzPingLow(MonitoringAccessPoint map, MonitoringAccessPointWizardDTO wizard, WebClient client, ObjectMapper mapper, String authToken, ExtZabbixDtoResponseTriggers listOfTriggersDevice, ExtZabbixDtoResponseService serviceData) throws JsonProcessingException {
        log.info("[   ] -> go for create services data :High ICMP ping response time:");
        Long trgRLow = Long.valueOf(listOfTriggersDevice.getResult().stream().filter(i -> i.getDescription().equals("High ICMP ping response time")).findFirst().get().getTriggerid());
        map.setDeviceTriggerResponseLow(trgRLow);
        WebClient.RequestHeadersSpec<?> xxxYRT = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoRequest("service.create",
                        new ExtZabbixDtoCreateNewService(wizard.getDevice().getHostName() + ": High ICMP ping response time", Long.valueOf(serviceData.getResult().get(0)), trgRLow),
                        42,
                        authToken
                )));
        String responseServiceDataResponseTime = xxxYRT.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseService serviceDataResponseTime = mapper.readValue(responseServiceDataResponseTime, ExtZabbixDtoResponseService.class);
        log.info("[   ] <- go for create services data :High ICMP ping response time: :: {}", serviceDataResponseTime);
    }

    private void zzzPingLost(MonitoringAccessPoint map, MonitoringAccessPointWizardDTO wizard, WebClient client, ObjectMapper mapper, String authToken, ExtZabbixDtoResponseTriggers listOfTriggersDevice, ExtZabbixDtoResponseService serviceData) throws JsonProcessingException {
        log.info("[   ] -> go for create services data :High ICMP ping loss:");
        Long trgRLost = Long.valueOf(listOfTriggersDevice.getResult().stream().filter(i -> i.getDescription().equals("High ICMP ping loss")).findFirst().get().getTriggerid());
        map.setDeviceTriggerResponseLost(trgRLost);
        WebClient.RequestHeadersSpec<?> xxxYL = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoRequest("service.create",
                        new ExtZabbixDtoCreateNewService(wizard.getDevice().getHostName() + ": High ICMP ping loss", Long.valueOf(serviceData.getResult().get(0)), trgRLost),
                        42,
                        authToken
                )));
        String responseServiceDataLoss = xxxYL.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseService serviceDataLoss = mapper.readValue(responseServiceDataLoss, ExtZabbixDtoResponseService.class);
        log.info("[   ] <- go for create services data :High ICMP ping loss: :: {}", serviceDataLoss);
    }

    private ExtZabbixDtoResponseService getExtZabbixDtoResponseService(WebClient client, ObjectMapper mapper, String authToken, ExtZabbixDtoResponseService service, String s, String s2, String s3) throws JsonProcessingException {
        log.info(s);
        WebClient.RequestHeadersSpec<?> xxxY = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoRequest("service.create",
                        new ExtZabbixDtoCreateNewService(s2, Long.valueOf(service.getResult().get(0))),
                        42,
                        authToken
                )));
        String responseServiceData = xxxY.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseService serviceData = mapper.readValue(responseServiceData, ExtZabbixDtoResponseService.class);
        log.info(s3, serviceData);
        return serviceData;
    }

    private ExtZabbixDtoResponseService getExtZabbixDtoResponseService(AccessPoint ap, WebClient client, ObjectMapper mapper, String authToken) throws JsonProcessingException {
        log.info("[   ] -> go for create services");
        WebClient.RequestHeadersSpec<?> xxx = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoRequest("service.create",
                        new ExtZabbixDtoCreateNewService(ap),
                        42,
                        authToken
                )));
        String serviceResponse = xxx.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseService service = mapper.readValue(serviceResponse, ExtZabbixDtoResponseService.class);
        log.info("[   ] <- go for create services:: {}", service);
        return service;
    }

    private ExtZabbixDtoResponseTriggers getExtZabbixDtoResponseTriggers(WebClient client, ObjectMapper mapper, String authToken, String s, String deviceId, String s2) throws JsonProcessingException {
        log.info(s);
        WebClient.RequestHeadersSpec<?> triggersDevice = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ExtZabbixDtoRequest("trigger.get", new ExtZabbixDtoGetTrgiggerParams(deviceId), 42, authToken)));
        String triggersResponce = triggersDevice.retrieve().bodyToMono(String.class).block();
        ExtZabbixDtoResponseTriggers listOfTriggersDevice = mapper.readValue(triggersResponce, ExtZabbixDtoResponseTriggers.class);
        log.info(s2);
        return listOfTriggersDevice;
    }

    private void createDevice(MonitoringAccessPoint map, MonitoringAccessPointWizardDTO wizard, WebClient client, String authToken) throws Exception {
        log.info("[   ] -> monitoring::wizard::data:: {}", wizard);
        log.info("[   ] -> create device");
        String device = insertIntoZabbix(client, authToken, wizard.getDevice());
        log.info("[   ] <- create device");
        map.setDeviceId(device);
        map.setDeviceName(wizard.getDevice().getHostName());
        map.setDeviceIp(wizard.getDevice().getIp());
    }

    private void createSensor(MonitoringAccessPoint map, MonitoringAccessPointWizardDTO wizard, WebClient client, String authToken) throws Exception {
        log.info("[   ] -> monitoring::wizard::data:: {}", wizard);
        log.info("[   ] -> create sensor");
        String sensor = insertIntoZabbix(client, authToken, wizard.getSensor());
        log.info("[   ] <- create sensor");
        map.setSensorId(sensor);
        map.setSensorName(wizard.getSensor().getHostName());
        map.setSensorIp(wizard.getSensor().getIp());
    }

    private String insertIntoZabbix(WebClient client, String authToken, @NotNull ExtZabbixDto zabbix) throws Exception {
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
