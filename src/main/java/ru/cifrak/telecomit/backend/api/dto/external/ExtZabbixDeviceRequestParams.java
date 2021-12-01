package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
public class ExtZabbixDeviceRequestParams {
    String hostids;
    String[] output = {"triggerid", "description", "priority"};
    Map<String, String> filter;

    public ExtZabbixDeviceRequestParams(String deviceId) {
        hostids = deviceId;
        filter = new HashMap<>();
//        Problem
        filter.put("value", "1");
//        Activated
        filter.put("status", "0");
    }
}
