package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

/**
 * DTO для поиска всех хостов
 */
@Value
public class ExtZabbixDtoGetHostsParams {
    String[] output = {"hostid", "host"};
    Search search;

    public ExtZabbixDtoGetHostsParams(String hostName) {
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
