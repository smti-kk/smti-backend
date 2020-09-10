package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;

@Data
public class ReportMapDTO {
    // ---                    UTM5
    private Integer pp;
    private Integer ucn;
    private String parent;
    private String location;
    private String address;
    private String contractor;
    private String organization;
    private String consumption;
    // ---                    Zabbix
    private String okTime;
    /**
     * Необходимо выдавать кол-во в минутах, а прилетает в секундах
     */
    private String problemTime;
}
