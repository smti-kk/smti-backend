package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.features.comparing.LocationFC;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.stream.Collectors;
@Data
public class FeatureExportDTO {
    private static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    private Integer pp;
    private String locationType;
    private String locationName;
    private String district;
    private String districtName;
    private Integer population;
    private String actual;
    private String planForOneYear;
    private String planForTwoYear;
    private String archive;
    private String OKATO;

    public FeatureExportDTO(@NotNull LocationFC locationFC, TcType tcType) {
        this.locationType = locationFC.getType();
        this.locationName = locationFC.getName();
        this.population = locationFC.getPopulation();
        this.district = locationFC.getLocationParent().getType();
        this.districtName = locationFC.getLocationParent().getName();
        this.actual = translateDataToString(locationFC, tcType, TcState.ACTIVE, currentYear);
        this.archive = translateDataToString(locationFC, tcType, TcState.ARCHIVE, currentYear - 1);
        this.planForOneYear = translateDataToString(locationFC, tcType, TcState.PLAN, currentYear + 1);
        this.planForTwoYear = translateDataToString(locationFC, tcType, TcState.PLAN, currentYear + 2);

        this.OKATO = locationFC.getOkato();
    }

    private String translateDataToString(LocationFC locationFC, TcType type, TcState state, int year) {
        return locationFC.getTechnicalCapabilities().stream()
                .filter(
                        tc -> tc.getType().equals(type) &&
                                tc.getState().equals(state) &&
                                tc.getGovYearComplete() == year
                )
                .map(tc -> tc.getOperator().getName() + (
                        type.equals(TcType.INET) ? tc.getTrunkChannel().getName() : tc.getTypeMobile().getName())
                )
                .collect(Collectors.joining(","));
    }
}
