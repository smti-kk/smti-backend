package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.domain.CatalogsTrunkChannelType;

@Data
public class TrunkChannelTypeDTO {
    private Integer id;
    private String name;

    public TrunkChannelTypeDTO(CatalogsTrunkChannelType catalogsTrunkChannelType) {
        this.id = catalogsTrunkChannelType.getId();
        this.name = catalogsTrunkChannelType.getName();
    }
}
