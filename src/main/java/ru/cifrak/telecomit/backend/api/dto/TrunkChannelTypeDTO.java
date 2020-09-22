package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.TypeTrunkChannel;

@Data
public class TrunkChannelTypeDTO {
    private Integer id;
    private String name;

    public TrunkChannelTypeDTO(TypeTrunkChannel catalogsTrunkChannelType) {
        this.id = catalogsTrunkChannelType.getId();
        this.name = catalogsTrunkChannelType.getName();
    }
}
