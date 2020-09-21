package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.*;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class ExelReportLocation {

    private Integer pp;
    private String locationType;
    private String locationName;
    private String district;
    private String districtName;
    private Integer population;
    private String ESPD;
    private String RSMO;
    private String ZSPD;
    private String SMO;
    private String cellular;
    private String internet;
    private String telephone;
    private String payphone;
    private String infomat;
    private String radio;
    private String TV;
    private String post;
    private String OKATO;


    public ExelReportLocation(@NotNull Location location) {

        this.locationType = location.getType();
        this.locationName = location.getName();
        this.population = location.getPopulation();
        this.district = location.getParent().getType();
        this.districtName = location.getParent().getName();
        this.cellular = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcAts)
                .map(tc -> tc.getOperator().getName())
                .collect(Collectors.joining(","));;

        this.ESPD = location.getOrganizations().stream()
                .anyMatch(org -> org.getAccessPoints().stream()
                        .anyMatch(ap -> ap instanceof ApESPD)
                ) ? "1" : "0";

        this.RSMO = location.getOrganizations().stream()
                .anyMatch(org -> org.getAccessPoints().stream()
                        .anyMatch(ap -> ap instanceof ApRSMO)
                ) ? "1" : "0";
        this.ZSPD = location.getOrganizations().stream()
                .anyMatch(org -> org.getAccessPoints().stream()
                        .anyMatch(ap -> ap instanceof ApZSPD)
                )? "1" : "0";
        this.SMO = location.getOrganizations().stream()
                .anyMatch(org -> org.getAccessPoints().stream()
                        .anyMatch(ap -> ap instanceof ApSMO)
                )? "1" : "0";

        this.internet = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcInternet)
                .map(tc -> tc.getOperator().getName() + " (" + Optional.ofNullable(((TcInternet) tc).getTrunkChannel()).map(TypeTrunkChannel::getName)
                        .orElse("Не выбрано") + ")")
                .collect(Collectors.joining(","));
        this.telephone = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcMobile)
                .map(tc -> tc.getOperator().getName() + " (" + ((TcMobile) tc).getType().getName() + ")")
                .collect(Collectors.joining(","));

        this.payphone = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcPayphone)
                .map(tc -> tc.getOperator().getName() + " (" + ((TcPayphone) tc).getQuantity() + ")")
                .collect(Collectors.joining(","));

        this.infomat = location.getTechnicalCapabilities().stream()
                .anyMatch(tc -> tc instanceof TcInfomat) ? "1" : "0";

        this.radio = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcRadio)
                .map(tc -> tc.getOperator().getName() + " (" + ((TcRadio) tc).signalAsString() + ")")
                .collect(Collectors.joining(","));

        this.TV = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcTv)
                .map(tc -> tc.getOperator().getName() + " (" + (((TcTv) tc).signalAsString()) + ")")
                .collect(Collectors.joining(","));

        this.post = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcPost)
                .map(tc -> tc.getOperator().getName() + " (" + ((TcPost) tc).getType() + ")")
                .collect(Collectors.joining(","));

        this.OKATO = location.getOkato();

    }

}
