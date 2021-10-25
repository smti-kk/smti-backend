package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FeatureEditAction {
    UPDATE ("UPDATE", "Изменение"),
    CREATE ("CREATE", "Создание"),
    DELETE ("DELETE", "Удаление");

    private String name;
    private String description;

    @Override
    public String toString() {
        return  (this.getDescription());
    }
}
