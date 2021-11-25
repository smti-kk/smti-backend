package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeLocation {
    REGION ("REGION", "край", "край", true),
    CITY_DISTRICT ("CITY_DISTRICT", "городской округ", "го", true),
    MUNICIPAL_DISTRICT ("MUNICIPAL_DISTRICT", "муниципальный округ", "мо", true),
    MUNICIPAL_AREA("MUNICIPAL_AREA", "муниципальный район", "р-н", true),
    CITY ("CITY", "город", "г", false),
    URBAN_SETTLEMENT ("URBAN_SETTLEMENT", "поселок городского типа", "пгт", false),
    VILLAGE ("VILLAGE", "деревня", "д", false),
    COUNTRYSIDE ("COUNTRYSIDE", "село", "с", false),
    SETTLEMENT ("SETTLEMENT", "поселок", "п", false);

    private final String name;
    private final String description;
    private final String shorted;
    private final boolean canBeParent;

    @Override
    public String toString() {
        return this.name();
    }
}
