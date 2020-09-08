package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

@Value
public class ExtZabbixDtoRequest {
    String jsonrpc = "2.0";
    String method;
    Object params;
    int id;
    String auth;

    public ExtZabbixDtoRequest(String method, Object params, int id, String auth) {
        this.method = method;
        this.params = params;
        this.id = id;
        this.auth = auth;
    }
}
