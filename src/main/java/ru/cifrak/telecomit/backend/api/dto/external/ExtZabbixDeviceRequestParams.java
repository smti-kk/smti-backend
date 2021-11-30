package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
@AllArgsConstructor
public class ExtZabbixDeviceRequestParams {
    String hostids;
    String[] output = {"triggerid", "description", "value", "priority"};
    Map<String, String> filter = new HashMap<>();
    public ExtZabbixDeviceRequestParams(String deviceId) {
        hostids = deviceId;
        filter.put("value","1");
    }
}
