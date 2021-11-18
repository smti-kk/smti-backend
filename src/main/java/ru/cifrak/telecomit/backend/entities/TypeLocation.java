package ru.cifrak.telecomit.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeLocation {
    REGION ("край"),
    CITY_DISTRICT ("городской округ"),
    MUNICIPAL_DISTRICT ("муниципальный округ"),
    MUNICIOAL_AREA ("муниципальный район"),
    CITY ("город"),
    URBAN_SETTLEMENT ("поселок городского типа"),
    VILLAGE ("деревня"),
    COUNTRYSIDE ("село"),
    SETTLEMENT ("поселок");

    private final String description;

    @Override
    public String toString() {
        return getDescription();
    }
}
