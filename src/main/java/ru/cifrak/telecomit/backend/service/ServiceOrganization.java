package ru.cifrak.telecomit.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.api.dto.MonitoringAccessPointWizardDTO;
import ru.cifrak.telecomit.backend.api.dto.response.ExternalSystemCreateStatusDTO;
import ru.cifrak.telecomit.backend.entities.APConnectionState;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;
import ru.cifrak.telecomit.backend.exceptions.NotAllowedException;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryJournalMAP;
import ru.cifrak.telecomit.backend.repository.RepositoryMonitoringAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;
import ru.cifrak.telecomit.backend.security.UTM5Config;
import ru.cifrak.telecomit.backend.security.ZabbixConfig;

import java.util.ArrayList;
import java.util.List;

@Slf4j

@Transactional
@Service
public class ServiceOrganization {
    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints rAccessPoints;
    private final RepositoryMonitoringAccessPoints rMonitoringAccessPoints;
    private final RepositoryJournalMAP rJournalMAP;
    private final ServiceExternalBlenders blenders;

    public ServiceOrganization(RepositoryOrganization rOrganization, RepositoryAccessPoints rAccessPoints, RepositoryMonitoringAccessPoints rMonitoringAccessPoints, RepositoryJournalMAP rJournalMAP, UTM5Config utm5Config, ZabbixConfig zabbixConfig, ServiceExternalBlenders blenders) {
        this.rOrganization = rOrganization;
        this.rAccessPoints = rAccessPoints;
        this.rMonitoringAccessPoints = rMonitoringAccessPoints;
        this.rJournalMAP = rJournalMAP;
        this.blenders = blenders;
    }

    public List<Organization> all() {
        return rOrganization.findAll();
    }

    public ExternalSystemCreateStatusDTO linkAccessPointWithMonitoringSystems(Integer id, Integer apid, MonitoringAccessPointWizardDTO wizard) throws NotAllowedException {
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
                rMonitoringAccessPoints.save(map);
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
}
