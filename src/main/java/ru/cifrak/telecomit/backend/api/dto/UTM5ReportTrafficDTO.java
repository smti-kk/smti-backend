package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;


@Data
public class UTM5ReportTrafficDTO {
    private Integer account_id;
    private Double base_cost;
    private Long bytes;
    private Double charge;
    private String login;
    private Integer tclass;
}
