package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class ExelReportLocation {
    private static Map<String, Integer> INTERNET_WEIGHT = new HashMap<String, Integer>() {{
        put("ростелеком", 1);
        put("дом.ру", 2);
        put("сибттк", 3);
        put("искра", 4);
        put("другие", 5);
    }};

    private static Map<String, Integer> MOBILE_WEIGHT = new HashMap<String, Integer>() {{
        put("мегафон", 1);
        put("теле2", 2);
        put("мтс", 3);
        put("билайн", 4);
    }};

    private Integer pp;
    private String locationType;
    private String locationName;
    private String district;
    private String districtName;
    private Integer population;
    private String ESPD;
    private String RSMO;
    private String EMSPD;
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
    private String fias;
    private String program;


    public ExelReportLocation(@NotNull Location location) {

        this.locationType = location.getType();
        this.locationName = location.getName();
        this.population = location.getPopulation();
        this.district = location.getParent().getType();
        this.districtName = location.getParent().getName();
        this.cellular = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcAts && tc.getState() == TcState.ACTIVE)
                .map(tc -> tc.getOperator().getName())
                .collect(Collectors.joining(","));

        this.ESPD = location.getOrganizations().stream()
                .anyMatch(org -> org.getAccessPoints().stream()
                        .anyMatch(ap -> ap instanceof ApESPD)
                ) ? "1" : "0";

        this.RSMO = location.getOrganizations().stream()
                .anyMatch(org -> org.getAccessPoints().stream()
                        .anyMatch(ap -> ap instanceof ApRSMO)
                ) ? "1" : "0";
        this.EMSPD = location.getOrganizations().stream()
                .anyMatch(org -> org.getAccessPoints().stream()
                        .anyMatch(ap -> ap instanceof ApEMSPD)
                )? "1" : "0";
        this.SMO = location.getOrganizations().stream()
                .anyMatch(org -> org.getAccessPoints().stream()
                        .anyMatch(ap -> ap instanceof ApSMO)
                )? "1" : "0";

        List<String> internetOperators = location.getTechnicalCapabilities().stream()
                .filter(tc1 -> tc1 instanceof TcInternet && tc1.getState() == TcState.ACTIVE)
                .map(tc1 -> tc1.getOperator().getName() + " (" + Optional.ofNullable(((TcInternet) tc1).getTrunkChannel()).map(TypeTrunkChannel::getName)
                        .orElse("Не выбрано") + ")")
                .collect(Collectors.toList());
        internetOperators.removeIf(o -> getInternetWeight(o) == null);
        internetOperators.sort(getInternetWeightComparator());
        this.internet = String.join(",", internetOperators);

        List<String> mobileOperators = location.getTechnicalCapabilities().stream()
                .filter(tc1 -> tc1 instanceof TcMobile && tc1.getState() == TcState.ACTIVE)
                .map(tc1 -> tc1.getOperator().getName() +
                        " (" + ((TcMobile) tc1).getType().getName() + ")" +
                        (((TcMobile) tc1).getType().getName().equals("4G") ? ":уд." : ""))
                .collect(Collectors.toList());
        mobileOperators.removeIf(o -> getMobileWeight(o) == null);
        mobileOperators.sort(getMobileWeightComparator());
        this.telephone = String.join(",", mobileOperators);

        this.payphone = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcPayphone && tc.getState() == TcState.ACTIVE)
                .map(tc -> tc.getOperator().getName() + " (" + ((TcPayphone) tc).getQuantity() + ")")
                .collect(Collectors.joining(","));

        this.infomat = location.getTechnicalCapabilities().stream()
                .anyMatch(tc -> tc instanceof TcInfomat && tc.getState() == TcState.ACTIVE) ? "1" : "0";

        this.radio = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcRadio && tc.getState() == TcState.ACTIVE)
                .map(tc -> tc.getOperator().getName() + " (" + ((TcRadio) tc).signalAsString() + ")")
                .collect(Collectors.joining(","));

        this.TV = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcTv && tc.getState() == TcState.ACTIVE)
                .map(tc -> tc.getOperator().getName() + " (" + (((TcTv) tc).signalAsString()) + ")")
                .collect(Collectors.joining(","));

        this.post = location.getTechnicalCapabilities().stream()
                .filter(tc -> tc instanceof TcPost && tc.getState() == TcState.ACTIVE)
                .map(tc -> tc.getOperator().getName() + " (" + ((TcPost) tc).getType() + ")")
                .collect(Collectors.joining(","));

        this.OKATO = location.getOkato();

        this.fias = location.getFias().toString();

        List<String> programs = new ArrayList<>();
        if (location.getOrganizations() != null) {
            for (Organization organization : location.getOrganizations()) {
                if (organization != null && organization.getAccessPoints() != null) {
                    for (AccessPoint accessPoint : organization.getAccessPoints()) {
                        if (accessPoint != null && accessPoint.getGovernmentDevelopmentProgram() != null) {
                            programs.add(accessPoint.getGovernmentDevelopmentProgram().getAcronym());
                        }
                    }
                }
            }
        }
        List<String> technicalCapabilityPrograms = new ArrayList<>();
        if (location.getTechnicalCapabilities() != null) {
            for (TechnicalCapability tc : location.getTechnicalCapabilities()) {
                if (tc != null && tc.getGovProgram() != null) {
                    String govProgram = tc.getGovProgram().getAcronym()
                            + (tc.getGovYearComplete() != null ? " " + String.valueOf(tc.getGovYearComplete()) : "");
                    if (!technicalCapabilityPrograms.contains(govProgram)) {
                        technicalCapabilityPrograms.add(govProgram);
                    }
                }
            }
        }
        programs.addAll(technicalCapabilityPrograms);
        this.program = String.join(",", programs);
    }

    @org.jetbrains.annotations.NotNull
    private Comparator<String> getInternetWeightComparator() {
        return (o1, o2) -> {
            int o1Weight = getInternetWeight(o1);
            int o2Weight = getInternetWeight(o2);
            return o1Weight - o2Weight;
        };
    }

    @org.jetbrains.annotations.NotNull
    private Comparator<String> getMobileWeightComparator() {
        return (o1, o2) -> {
            int o1Weight = getMobileWeight(o1);
            int o2Weight = getMobileWeight(o2);
            return o1Weight - o2Weight;
        };
    }

    private Integer getInternetWeight(String operator) {
        return INTERNET_WEIGHT.get(operator.substring(0, operator.indexOf("(")).trim().toLowerCase());
    }

    private Integer getMobileWeight(String operator) {
        return MOBILE_WEIGHT.get(operator.substring(0, operator.indexOf("(")).trim().toLowerCase());
    }
}
