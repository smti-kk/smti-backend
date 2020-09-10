package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;

@Data
public class ZabbixReportDTO {
    private Long serviceId;
    private String okTime;
    /**
     * Необходимо выдавать кол-во в минутах, а прилетает в секундах
     */
    private String problemTime;
}
