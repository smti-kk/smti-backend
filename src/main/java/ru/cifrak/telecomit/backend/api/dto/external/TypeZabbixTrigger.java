package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeZabbixTrigger {
    ENERGY ("unavailable by icmp ping energy"),
    LOST ("high icmp ping loss"),
    LOW ("high icmp ping response time"),
    UNAVAILABLE ("unavailable by icmp ping"),
    DOOR ("door open alarm"),
    PORT ("ether5");

    private String description;
}
