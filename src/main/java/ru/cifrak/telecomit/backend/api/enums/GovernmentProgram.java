package ru.cifrak.telecomit.backend.api.enums;

public enum GovernmentProgram {
    ESPD(1, "ЕСПД"),
    SMO(2, "СЗО");

    private Integer id;
    private String shortName;

    GovernmentProgram(Integer id, String shortName) {
        this.id = id;
        this.shortName = shortName;
    }

    public Integer getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }
}
