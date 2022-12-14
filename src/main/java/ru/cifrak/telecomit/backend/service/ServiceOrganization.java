package ru.cifrak.telecomit.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.api.dto.FunCustomerDto;
import ru.cifrak.telecomit.backend.api.dto.MonitoringAccessPointWizardDTO;
import ru.cifrak.telecomit.backend.api.dto.UTM5ReportTrafficDTO;
import ru.cifrak.telecomit.backend.api.dto.external.ExtZabbixHost;
import ru.cifrak.telecomit.backend.api.dto.external.ExtZabbixTrigger;
import ru.cifrak.telecomit.backend.api.dto.response.ExternalSystemCreateStatusDTO;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;
import ru.cifrak.telecomit.backend.exceptions.NotAllowedException;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.service.storage.FileSystemStorageService;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j

@Transactional
@Service
public class ServiceOrganization {
    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints rAccessPoints;
    private final RepositoryMonitoringAccessPoints rMonitoringAccessPoints;
    private final RepositoryJournalMAP rJournalMAP;

    private final RepositoryFunCustomer rFunCustomer;
    private final ServiceExternalBlenders blenders;
    private final ServiceExternalZabbix sZabbix;
    private final ServiceExternalReports sReports;
    private final ServiceMonitoringNotification serviceMonitoringNotification;
    private final FileSystemStorageService fileSystemStorageService;

    public ServiceOrganization(RepositoryOrganization rOrganization,
                               RepositoryAccessPoints rAccessPoints,
                               RepositoryMonitoringAccessPoints rMonitoringAccessPoints,
                               RepositoryJournalMAP rJournalMAP,
                               RepositoryFunCustomer repositoryFunCustomer,
                               ServiceExternalBlenders blenders,
                               ServiceExternalZabbix sZabbix,
                               ServiceExternalReports sReports,
                               ServiceMonitoringNotification serviceMonitoringNotification,
                               FileSystemStorageService fileSystemStorageService) {
        this.rOrganization = rOrganization;
        this.rAccessPoints = rAccessPoints;
        this.rMonitoringAccessPoints = rMonitoringAccessPoints;
        this.rJournalMAP = rJournalMAP;
        this.rFunCustomer = repositoryFunCustomer;
        this.blenders = blenders;
        this.sZabbix = sZabbix;
        this.sReports = sReports;
        this.serviceMonitoringNotification = serviceMonitoringNotification;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    public List<Organization> all() {
        return rOrganization.findAll();
    }

    public ExternalSystemCreateStatusDTO linkAccessPointWithMonitoringSystems(Integer id,
                                                                              Integer apid,
                                                                              MonitoringAccessPointWizardDTO wizard,
                                                                              User user) throws NotAllowedException {
        // xx.xx.xx. тут надо сходить по идее в БД и посмотреть а есть ли у нас данные наши.
        AccessPoint ap = rAccessPoints.getOne(apid);
        JournalMAP jjmap = rJournalMAP.findByAp_Id(ap.getId());
        MonitoringAccessPoint map;
        if (jjmap == null || jjmap.getMap() == null) {
            if (jjmap == null) {
                jjmap = new JournalMAP();
            }
            map = new MonitoringAccessPoint();
        } else {
            map = jjmap.getMap();
        }
        if (ap.getOrganization().getId().equals(id)) {
            List<String> errors = new ArrayList<>();
            // это мы заводим в УТМ5
            if (wizard.getNetworks() != null && !wizard.getNetworks().isEmpty()) {
                ap.setNetworks(wizard.getNetworks());
                rAccessPoints.save(ap);
                try {
                    ap.setNetworks(wizard.getNetworks());
                    blenders.linkWithUTM5(ap, map);
                    log.info("(>) save monitoring access point");
                    map.setCreateDatetime(LocalDateTime.now(ZoneId.systemDefault()));
                    map = rMonitoringAccessPoints.save(map);
                    log.info("(<) save monitoring access point");
                } catch (Exception e) {
                    errors.add("Система UTM вернула ошибку: " + e.getMessage());
                }
            } else {
                errors.add("Список сетей ПУСТОЙ для точки подключения. Это необходимый параметр.");
            }
            // это мы заводим в заббикс
            try {
                blenders.linkWithZabbix(ap, map, wizard);
                log.info("(>) save monitoring access point");
                map.setCreateDatetime(LocalDateTime.now(ZoneId.systemDefault()));
                rMonitoringAccessPoints.save(map);
                log.info("(<) save monitoring access point");
            } catch (Exception e) {
                errors.add("ZABBIX:error: " + e.getMessage());
            }

            // это сохранение в журнале точек бд
            log.info("(>) save journal map");
            jjmap.setAp(ap);
            jjmap.setMap(map);
            rJournalMAP.save(jjmap);
            ap.setConnectionState(APConnectionState.ACTIVE);
            rAccessPoints.save(ap);
            log.info("(<) save journal map");
            if (errors.isEmpty()) {
                addMonitoringNotification(ap, user);
                return new ExternalSystemCreateStatusDTO("Точка поставлена на мониторинг");
            } else if (errors.size() >= 2) {
                return new ExternalSystemCreateStatusDTO("Точку НЕУДАЛОСЬ поставить на мониторинг", errors);
            } else {
                addMonitoringNotification(ap, user);
                return new ExternalSystemCreateStatusDTO("Точка поставлена на мониторинг с ограничениями", errors);
            }

        } else {
            throw new NotAllowedException("You cannot init Access Point in non belonging Organization");
        }
    }

    private void addMonitoringNotification(AccessPoint ap, User user) {
        if (user.getRoles().contains(UserRole.CONTRACTOR)) {
            serviceMonitoringNotification.addMonitoringNotification(ap);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void autoMonitoringAccesspointDownloadedData() throws JsonProcessingException {
        log.info("[application]-> This is going for bytes in UTM5");
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Krasnoyarsk"));
        LocalDate previous = now.minus(1, ChronoUnit.DAYS);
        Long till = now.atStartOfDay(ZoneId.of("Asia/Krasnoyarsk")).toInstant().toEpochMilli() / 1000;
        Long from = previous.atStartOfDay(ZoneId.of("Asia/Krasnoyarsk")).toInstant().toEpochMilli() / 1000;
        List<UTM5ReportTrafficDTO> data = sReports.getReportFromUTM5(from, till);
        log.trace("report: {}", data);
        Map<Integer, UTM5ReportTrafficDTO> utm5Data = data.stream().filter(i -> i.getTclass().equals(10)).collect(Collectors.toMap(UTM5ReportTrafficDTO::getAccount_id, item -> item));
        List<JournalMAP> jmaps = rJournalMAP.findAll();

        jmaps = jmaps.stream()
                .peek(jmap -> {
                            if (utm5Data.get(jmap.getMap().getIdAccount()) != null) {
                                jmap.getMap().setLastDayTraffic(utm5Data.get(jmap.getMap().getIdAccount()).getBytes());
                                jmap.getMap().setTimeTraffic(LocalDateTime.now());
                            }
                        }
                ).collect(Collectors.toList());

        for (JournalMAP j : jmaps) {
            if (j.getMap().getLastDayTraffic() != null) rMonitoringAccessPoints.save(j.getMap());
        }
        log.info("[application]<- This is going for bytes in UTM5");
    }

    @Scheduled(cron = "0 0 * * * *")
    public void autoMonitoringAccessPointStatus() throws IOException {
        log.info("--> going for activity status in zabbix");
        PrintWriter pw = getPrintWriter();
        List<MonitoringAccessPoint> maps = rMonitoringAccessPoints.findAll();
        Map<Integer, Integer> jmaps = rJournalMAP.findAll().stream().collect(Collectors.toMap(
                jmap -> jmap.getMap().getId(),
                jmap -> jmap.getAp().getId()));
        Map<String, ExtZabbixHost> devices = sZabbix.getHostsInProblemState(maps);
        for (MonitoringAccessPoint mapNotManaged : maps) {
            Optional<MonitoringAccessPoint> mapOptional = rMonitoringAccessPoints.findById(mapNotManaged.getId());
            if (mapOptional.isPresent()) {
                MonitoringAccessPoint map = mapOptional.get();
                ExtZabbixHost problemDevice = devices.get(map.getDeviceId());
                ExtZabbixHost problemSensor = devices.get(map.getSensorId());
                if (problemDevice == null && problemSensor == null) {
                    map.setConnectionState(APConnectionState.ACTIVE);
                    map.setProblemDefinition("");
                    map.setImportance(null);
                } else {
                    ExtZabbixTrigger triggerUnavailable = problemDevice != null
                            ? problemDevice.triggerUnavailable()
                            : null;
                    if (triggerUnavailable != null) {
                        map.setConnectionState(APConnectionState.DISABLED);
                        map.setProblemDefinition(triggerUnavailable.getDescription());
                        map.setImportance(triggerUnavailable.getImportance());
                    } else {
                        map.setConnectionState(APConnectionState.PROBLEM);
                        map.setProblemDefinition(getProblemDefinition(problemDevice, problemSensor));
                        map.setImportance(getImportance(problemDevice, problemSensor));
                    }
                }
                map.setTimeState(LocalDateTime.now(ZoneId.systemDefault()));
                String smap = getString(map, jmaps);
                if (!smap.isEmpty()) {
                    pw.print(smap);
                }
            }
        }
        pw.close();
        doOriginal();
        log.info("<-- going for activity status in zabbix");
    }



    @NotNull
    private String getString(MonitoringAccessPoint map, Map<Integer, Integer> jmaps) {
        StringJoiner sj = new StringJoiner("::--::");
        Integer apId = jmaps.get(map.getId());
        Integer mapId = map.getId();
        if (apId != null) {
            sj.add(String.valueOf(apId));
            sj.add(String.valueOf(mapId));
            sj.add(map.getConnectionState().name());
            sj.add(map.getProblemDefinition());
            sj.add(map.getImportance() == null ? "" : map.getImportance().name());
            sj.add("::!!::");
        }
        return sj.toString();
    }

    @NotNull
    private PrintWriter getPrintWriter() throws IOException {
        File upload1 = new File(fileSystemStorageService.getRootLocation().toFile(), "upload1");
        upload1.delete();
        FileWriter fileWriter = new FileWriter(upload1);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        return printWriter;
    }

    private void doOriginal() throws IOException {
        File upload1 = new File(fileSystemStorageService.getRootLocation().toFile(), "upload1");
        File upload = new File(fileSystemStorageService.getRootLocation().toFile(), "upload");
        upload.delete();
        Files.copy(upload1.toPath(), upload.toPath());
        upload1.delete();
    }

    public void updateAccessPointState() throws FileNotFoundException {
        File upload = new File(fileSystemStorageService.getRootLocation().toFile(), "upload");
        Map<Integer, ApMonitoring> mapAp = new HashMap<>();
        if (upload.exists()) {
            Scanner scanner = new Scanner(upload);
            scanner.useDelimiter("::!!::");
            Integer apId;
            Integer mapId;
            while (scanner.hasNext()) {
                String string = scanner.next();
                String[] strings = string.split("::--::");
                if (strings.length == 5) {
                    apId = Integer.parseInt(strings[0]);
                    mapId = Integer.parseInt(strings[1]);
                    mapAp.put(apId,
                            new ApMonitoring(
                                    apId,
                                    mapId,
                                    APConnectionState.valueOf(strings[2]),
                                    strings[3],
                                    strings[4].isEmpty() ? null : ImportanceProblemStatus.valueOf(strings[4])
                            )
                    );
                } else if (strings.length == 3) {
                    apId = Integer.parseInt(strings[0]);
                    mapId = Integer.parseInt(strings[1]);
                    mapAp.put(apId,
                            new ApMonitoring(
                                    apId,
                                    mapId,
                                    APConnectionState.ACTIVE,
                                    "",
                                    null
                            )
                    );
                }
            }
        }
        List<AccessPoint> accessPointsAll = rAccessPoints.findAll();
        for (AccessPoint pointNotManaged : accessPointsAll) {
            Optional<AccessPoint> pointOptional = rAccessPoints.findById(pointNotManaged.getId());
            if (pointOptional.isPresent()) {
                AccessPoint point = pointOptional.get();
                ApMonitoring apMonitoring = mapAp.get(point.getId());
                if (apMonitoring == null) {
                    if (point.getConnectionState() != APConnectionState.NOT_MONITORED) {
                        point.setConnectionState(APConnectionState.NOT_MONITORED);
                        rAccessPoints.save(point);
                    }
                } else {
                    if (apMonitoring.getApConnectionState() != point.getConnectionState()) {
                        point.setConnectionState(apMonitoring.getApConnectionState());
                        rAccessPoints.save(point);
                    }
                }
            }
        }
        List<MonitoringAccessPoint> maps = rMonitoringAccessPoints.findAll();
        Map<Integer, Integer> jmaps = rJournalMAP.findAll().stream().collect(Collectors.toMap(
                jmap -> jmap.getMap().getId(),
                jmap -> jmap.getAp().getId()));
        for (MonitoringAccessPoint mapNotManaged : maps) {
            Optional<MonitoringAccessPoint> mapOptional = rMonitoringAccessPoints.findById(mapNotManaged.getId());
            if (mapOptional.isPresent()) {
                MonitoringAccessPoint map = mapOptional.get();
                ApMonitoring apMonitoring = mapAp.get(jmaps.get(map.getId()));
                if (apMonitoring != null) {
                    if (apMonitoring.getApConnectionState() != map.getConnectionState()
                    || !apMonitoring.getProblemDefinition().equals(map.getProblemDefinition())
                    || apMonitoring.getImportance() != map.getImportance()) {
                        map.setConnectionState(apMonitoring.getApConnectionState());
                        map.setProblemDefinition(apMonitoring.getProblemDefinition());
                        map.setImportance(apMonitoring.getImportance());
                        rMonitoringAccessPoints.save(map);
                    }
                }
            }
        }
        upload.delete();
    }

    @NotNull
    private String getProblemDefinition(@Nullable ExtZabbixHost problemDevice, @Nullable ExtZabbixHost problemSensor) {
        List<String> result = new ArrayList<>();
        if (problemDevice != null) {
            result = problemDevice.getTriggers().stream().map(ExtZabbixTrigger::getDescription)
                    .collect(Collectors.toList());
        }
        if (problemSensor != null) {
            result.addAll(problemSensor.getTriggers().stream().map(ExtZabbixTrigger::getDescription)
                    .collect(Collectors.toList()));
        }
        return String.join(", ", result);
    }

    @NotNull
    private ImportanceProblemStatus getImportance(@Nullable ExtZabbixHost problemDevice,
                                                  @Nullable ExtZabbixHost problemSensor) {
        ImportanceProblemStatus result = ImportanceProblemStatus.LOW;
        if (problemDevice != null) {
            for (ExtZabbixTrigger t : problemDevice.getTriggers()) {
                result = result.compareTo(t.getImportance()) > 0 ? result : t.getImportance();
            }
        }
        if (problemSensor != null) {
            for (ExtZabbixTrigger t : problemSensor.getTriggers()) {
                result = result.compareTo(t.getImportance()) > 0 ? result : t.getImportance();
            }
        }
        return result;
    }

    public List<FunCustomerDto> getListFunCustomer(String apType) {
        return rFunCustomer.findAll().stream()
                .filter(funCustomer ->
                                funCustomer.getApType().getName().equals(apType) ||
                                funCustomer.getApType().getName().equals(TypeAccessPoint.GENERAL.getName()))
                .map(FunCustomerDto::new).collect(Collectors.toList());
    }

    public FunCustomerDto getFunCustomer(Integer id) {
        return rFunCustomer.findById(id).map(FunCustomerDto::new)
                .orElseThrow( () -> { throw new NotFoundException(
                        "Не найден функциональный заказчик по данному идентификатору [" + id + "]!"
                ); } );
    }

    public FunCustomerDto createOrUpdateFunCustomer(FunCustomerDto dto) {
        return new FunCustomerDto(rFunCustomer.saveAndFlush(new FunCustomer(dto)));
    }

}
