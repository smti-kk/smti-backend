package ru.cifrak.telecomit.backend.entities.locationsummary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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
