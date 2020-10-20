package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;

@Data
public class ReportMapDTO {
    private Integer pp;
    // ---                    AP
    private String internetAccessType;
    private String parent;
    private String location;
    private String address;
    private String contractor;
    private String organization;
    private String networks;
    private String NA;
    private String mask="/29";
    // ---                    UTM5
    private Integer ucn;
    private String consumption;
    // ---                    Zabbix
    private String sla;
    private String zabbixDeviceIp;
    private String zabbixDeviceName;
    /**
     * Необходимо выдавать кол-во в минутах, а прилетает в секундах
     */
    private String problemTime;

}
