package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

/**
 * DTO для поиска параметров конкретного Хоста
 */
@Value
public class ExtZabbixDtoGetHostParams {
    String[] output = {"hostid", "host"};
    String[] selectInterfaces = {"interfaceid", "ip"};
    String[] selectTriggers = {"triggerid", "description"};
    Search search;

    public ExtZabbixDtoGetHostParams(String hostName) {
        search = new Search(hostName);
    }

    @Value
    class Search {
        String host;
        public Search(String host) {
            this.host = host;
        }
    }
}
