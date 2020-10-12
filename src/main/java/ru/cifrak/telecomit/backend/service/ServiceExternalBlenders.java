package ru.cifrak.telecomit.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.api.dto.MonitoringAccessPointWizardDTO;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;

@Slf4j
@Service
public class ServiceExternalBlenders {
    private final ServiceExternalUTM5 utm5;
    private final ServiceExternalZabbix zabbix;

    public ServiceExternalBlenders(ServiceExternalUTM5 utm5, ServiceExternalZabbix zabbix) {
        this.utm5 = utm5;
        this.zabbix = zabbix;
    }

    public void linkWithUTM5(AccessPoint ap, MonitoringAccessPoint map) throws JsonProcessingException {
        utm5.linking(ap, map);
    }

    public void linkWithZabbix(AccessPoint ap, MonitoringAccessPoint map, MonitoringAccessPointWizardDTO wizard) throws Exception {
        zabbix.linking(ap, map, wizard);
    }


}
