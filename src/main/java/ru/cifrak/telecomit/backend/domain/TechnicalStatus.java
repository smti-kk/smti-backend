package ru.cifrak.telecomit.backend.domain;

public enum TechnicalStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    DECLINED("declined"),
    OUTDATED("outdated");

    private String name;

    TechnicalStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
