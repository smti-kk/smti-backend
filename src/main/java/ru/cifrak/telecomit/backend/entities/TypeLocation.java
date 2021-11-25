package ru.cifrak.telecomit.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeLocation {
    REGION ("край", true),
    CITY_DISTRICT ("городской округ", true),
    MUNICIPAL_DISTRICT ("муниципальный округ", true),
    MUNICIOAL_AREA ("муниципальный район", false),
    CITY ("город", false),
    URBAN_SETTLEMENT ("поселок городского типа", false),
    VILLAGE ("деревня", false),
    COUNTRYSIDE ("село", false),
    SETTLEMENT ("поселок", false);

    private final String description;
    private final boolean canBeParent;

    @Override
    public String toString() {
        return this.name();
    }
}
