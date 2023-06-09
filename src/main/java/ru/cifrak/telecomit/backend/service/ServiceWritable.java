package ru.cifrak.telecomit.backend.service;

import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEdit;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;

import java.util.Set;

// TODO: переделать определение данного сервиса во всех местах на ServiceWritableTc
public interface ServiceWritable {
    /**
     * редактирование активных тех. возможностей и точек подключения у локации
     * @param features отредактированные тех. возможности
     * @param locationId локация, тех. возможности которой редактируются
     */
    void editLocationFeatures(Set<FeatureEdit> features, Integer locationId);
    void editMunicipalityLocationFeatures(Set<FeatureEdit> features, Integer locationId);
    Set<FeatureEdit> defineEditActions(Set<WritableTc> features, Integer locationId);


}
