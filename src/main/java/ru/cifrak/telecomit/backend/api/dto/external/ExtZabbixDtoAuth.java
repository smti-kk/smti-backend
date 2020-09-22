package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

@Value
public class ExtZabbixDtoAuth {
    String jsonrpc = "2.0";
    String method = "user.login";
    Data params;
    int id = 100;
    Object auth = null;

    public ExtZabbixDtoAuth(String login, String password) {
        params = new Data(login, password);
    }

    @Value
    class Data {
        String user;
        String password;
    }
}
