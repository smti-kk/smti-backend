package ru.cifrak.telecomit.backend.service;

import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEdit;
import ru.cifrak.telecomit.backend.entities.locationsummary.FeatureEditAction;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FeatureEditHashSet extends HashSet<FeatureEdit> {
    /**
     * Установление статусов УДАЛЕНИЕ, СОЗДАНИЕ, РЕДАКТИРОВАНИЕ тех. возможностей.
     * Если в текущих тех. возможностях отсутствует одна из тех. возможностей из запроса, то
     * она добавляется и заносится в базу.
     * Если в тех. возможностях из запроса отсутствует одна из текущих тех. возможностей, то
     * то эта текущая тех. возможность получает статус архив
     * Если в тех. возможностях из запроса присутствует тех. возможность с еквивалентным id, но с разными
     * полями, то старой тех. возможности присуждается статус архив, а новая получает новый id и сохраняется
     */
    public FeatureEditHashSet(Set<WritableTc> features, List<WritableTc> existActiveLocationFeatures) {
        existActiveLocationFeatures.forEach(existActiveLocationFeature -> {
            WritableTc newFeatureByOldFeatureId = features.stream()
                    .filter(elf -> Objects.equals(elf.getId(), existActiveLocationFeature.getId()))
                    .findAny()
                    .orElse(null);
            if (newFeatureByOldFeatureId == null) {
                add(new FeatureEdit(existActiveLocationFeature, FeatureEditAction.DELETE));
            } else if (!newFeatureByOldFeatureId.hasSameEqualsProperties(existActiveLocationFeature)) {
                features.remove(newFeatureByOldFeatureId);
                add(new FeatureEdit(
                        existActiveLocationFeature,
                        newFeatureByOldFeatureId.cloneWithNullId()
                ));
            } else if (newFeatureByOldFeatureId.hasSameEqualsProperties(existActiveLocationFeature)) {
                features.remove(newFeatureByOldFeatureId);
            }
        });
        features.forEach(f -> add(new FeatureEdit(f, FeatureEditAction.CREATE)));
    }
}
