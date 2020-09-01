package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;
import ru.cifrak.telecomit.backend.entities.AccessPoint;

@Value
public class ExtZabbixDtoCreateNewService {
    String name;
    Integer algorithm;
    Integer showsla;
    Double goodsla;
    Integer sortorder;
    Long parentid;
    Long triggerid;

    public ExtZabbixDtoCreateNewService(String name, Integer algorithm, Integer showsla, Double goodsla, Integer sortorder, Long parentid, Long triggerid) {
        this.name = name;
        this.algorithm = algorithm;
        this.showsla = showsla;
        this.goodsla = goodsla;
        this.sortorder = sortorder;
        this.parentid = parentid;
        this.triggerid = triggerid;
    }

    public ExtZabbixDtoCreateNewService(String name, Long parentid, Long triggerid) {
        this(name, 1, 1, 99.99, 1, parentid, triggerid);
    }

    public ExtZabbixDtoCreateNewService(String name, Long parentid) {
        this(name, 1, 1, 99.99, 1, parentid, null);
    }

    public ExtZabbixDtoCreateNewService(String name) {
        this(name, 1, 1, 99.99, 1, null, null);
    }

    public ExtZabbixDtoCreateNewService(final AccessPoint ap) {
        this(new StringBuilder()
                .append("telecom:org:")
                .append(ap.getOrganization().getId())
                .append(":")
                .append(ap.getOrganization().getAddress())
                .append(":ap:")
                .append(ap.getId())
                .append(":")
                .append(ap.getAddress())
                .toString(), 1, 1, 99.99, 1, null, null);
    }
}
