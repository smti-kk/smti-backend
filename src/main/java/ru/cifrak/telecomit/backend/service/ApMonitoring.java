package ru.cifrak.telecomit.backend.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.cifrak.telecomit.backend.entities.APConnectionState;
import ru.cifrak.telecomit.backend.entities.ImportanceProblemStatus;

@Getter
@AllArgsConstructor
public class ApMonitoring {
    private Integer apId;

    private Integer mapId;

    private APConnectionState apConnectionState;

    private String problemDefinition;

    private ImportanceProblemStatus importance;
}
