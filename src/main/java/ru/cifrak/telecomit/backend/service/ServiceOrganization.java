package ru.cifrak.telecomit.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cifrak.telecomit.backend.api.dto.MonitoringAccessPointWizardDTO;
import ru.cifrak.telecomit.backend.api.dto.OrganizationDTO;
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

/*    public OrganizationDTO getOrganizationById(Integer id) {
        return rOrganization.findById(id).map(OrganizationDTO::new).orElse(null);
    }*/

    public String initializeMonitoringOnAp(Integer id, Integer apid, MonitoringAccessPointWizardDTO wizard) throws Exception {
        AccessPoint ap = rAccessPoints.getOne(apid);
        MonitoringAccessPoint map = new MonitoringAccessPoint();
        JournalMAP jmap = new JournalMAP();
        jmap.setAp(ap);
        jmap.setActive(Boolean.TRUE);
        if (ap.getOrganization().getId().equals(id)) {
            try {
//                blenders.insertIntoUTM5(ap, map);
            } catch (Exception e) {
                throw new Exception("UTM5:error: " + e.getMessage());
            }
            try {
                blenders.insertIntoZabbix(ap, map, wizard);
            } catch (Exception e) {
                throw new Exception("ZABBIX:error: " + e.getMessage());
            }
            log.info("[  >] save monitoring access point");
            rMonitoringAccessPoints.save(map);
            log.info("[  <] save monitoring access point");
            jmap.setMap(map);
            log.info("[  >] save journal map");
            rJournalMAP.save(jmap);
            log.info("[  <] save journal map");
            return "Access Point has bean initialized in monitorings.";
        } else {
            throw new Exception("You cannot init Access Point in non belonging Organization");
        }
    }
}
