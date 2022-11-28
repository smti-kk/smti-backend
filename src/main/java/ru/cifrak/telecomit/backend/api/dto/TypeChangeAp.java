package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeChangeAp {

    TRANSFER   ("Перенос", "Перенос"),
    CONNECTION ("Подключение", "Подключение"),
    INCREASE   ("Увеличение", "Увеличение");

    private String key;
    private String value;
}
