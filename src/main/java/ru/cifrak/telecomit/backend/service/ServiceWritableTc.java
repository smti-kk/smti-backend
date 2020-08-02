package ru.cifrak.telecomit.backend.service;

import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEdit;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;

import java.util.Set;

public interface ServiceWritableTc {
    /**
     * редактирование активных тех. возможностей у локации
     * @param features отредактированные тех. возможности
     * @param locationId локация, тех. возможности которой редактируются
     */
    void editLocationFeatures(Set<FeatureEdit> features, Integer locationId);
    Set<FeatureEdit> defineEditActions(Set<WritableTc> features, Integer locationId);
}
