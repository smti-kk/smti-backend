package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeZabbixTrigger {
    LOST ("high icmp ping loss", true),
    LOW ("high icmp ping response time", true),
    UNAVAILABLE ("unavailable by icmp ping", true),
    PORT ("ether5", true),
    ENERGY ("voltagedetector alarm", false),
    DOOR ("door open alarm", false);

    private final String description;
    private final boolean deviceTrigger;
}
