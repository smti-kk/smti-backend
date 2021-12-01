package ru.cifrak.telecomit.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.api.dto.MonitoringAccessPointWizardDTO;
import ru.cifrak.telecomit.backend.api.dto.UTM5ReportTrafficDTO;
import ru.cifrak.telecomit.backend.api.dto.external.ExtZabbixHost;
import ru.cifrak.telecomit.backend.api.dto.external.ExtZabbixTrigger;
import ru.cifrak.telecomit.backend.api.dto.response.ExternalSystemCreateStatusDTO;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;
import ru.cifrak.telecomit.backend.exceptions.NotAllowedException;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryJournalMAP;
import ru.cifrak.telecomit.backend.repository.RepositoryMonitoringAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j

@Transactional
@Service
public class ServiceOrganization {
    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints rAccessPoints;
    private final RepositoryMonitoringAccessPoints rMonitoringAccessPoints;
    private final RepositoryJournalMAP rJournalMAP;
    private final ServiceExternalBlenders blenders;
    private final ServiceExternalZabbix sZabbix;
    private final ServiceExternalReports sReports;
    private final ServiceMonitoringNotification serviceMonitoringNotification;

    public ServiceOrganization(RepositoryOrganization rOrganization,
                               RepositoryAccessPoints rAccessPoints,
                               RepositoryMonitoringAccessPoints rMonitoringAccessPoints,
                               RepositoryJournalMAP rJournalMAP,
                               ServiceExternalBlenders blenders,
                               ServiceExternalZabbix sZabbix,
                               ServiceExternalReports sReports,
                               ServiceMonitoringNotification serviceMonitoringNotification) {
        this.rOrganization = rOrganization;
        this.rAccessPoints = rAccessPoints;
        this.rMonitoringAccessPoints = rMonitoringAccessPoints;
        this.rJournalMAP = rJournalMAP;
        this.blenders = blenders;
        this.sZabbix = sZabbix;
        this.sReports = sReports;
        this.serviceMonitoringNotification = serviceMonitoringNotification;
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
        boolean new_map = false;
        if (jjmap == null || jjmap.getMap() == null) {
            if (jjmap == null) {
                jjmap = new JournalMAP();
            }
            map = new MonitoringAccessPoint();
            new_map = true;
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
                    map = rMonitoringAccessPoints.save(map);
                    if (new_map) {
                        addMonitoringNotification(ap, user);
                        new_map = false;
                    }
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
                rMonitoringAccessPoints.save(map);
                if (new_map) {
                    addMonitoringNotification(ap, user);
                }
                log.info("(<) save monitoring access point");
            } catch (Exception e) {
                errors.equals("ZABBIX:error: " + e.getMessage());
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
                return new ExternalSystemCreateStatusDTO("Точка поставлена на мониторинг");
            } else if (errors.size() >= 2) {
                return new ExternalSystemCreateStatusDTO("Точку НЕУДАЛОСЬ поставить на мониторинг", errors);
            } else {
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
    public void autoMonitoringAccessPointStatus() throws JsonProcessingException {
        log.info("--> going for activity status in zabbix");
        List<MonitoringAccessPoint> maps = rMonitoringAccessPoints.findAll();
        Map<String, ExtZabbixHost> devices = sZabbix.getHostsInProblemState(maps);
        maps.forEach(map -> {
            ExtZabbixHost problemDevice = devices.get(map.getDeviceId());
            ExtZabbixHost problemSensor = devices.get(map.getSensorId());
            if (problemDevice == null && problemSensor == null) {
                map.setConnectionState(APConnectionState.ACTIVE);
            } else if (problemDevice != null && problemDevice.triggerUnavailableExists()) {
                map.setConnectionState(APConnectionState.DISABLED);
            } else {
                map.setConnectionState(APConnectionState.PROBLEM);
                map.setProblemDefinition(getProblemDefinition(problemDevice, problemSensor));
            }
        });
        log.info("<-- going for activity status in zabbix");
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
}
