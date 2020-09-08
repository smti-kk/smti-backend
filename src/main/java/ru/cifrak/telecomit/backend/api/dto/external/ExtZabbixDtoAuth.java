package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

@Value
public class ExtZabbixDtoAuth {
    String jsonrpc = "2.0";
    String method ="user.login";
    Data params = new Data();
    int id = 100;
    Object auth = null;

    @Value
    class Data{
            String user = "api";
            String password = "Gjktnftv110";
    }
}
