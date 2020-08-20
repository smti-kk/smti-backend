package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;

@Data
public class ReportMapDTO {
    private Integer pp;
    private Integer ucn;
    private String parent;
    private String location;
    private String address;
    private String contractor;
    private String organization;
    private String consumption;
}
