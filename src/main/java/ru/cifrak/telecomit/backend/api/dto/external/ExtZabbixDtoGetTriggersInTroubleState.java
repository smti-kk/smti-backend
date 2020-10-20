package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class ExtZabbixDtoGetTriggersInTroubleState {
    List<String> triggerids;
    String output [] =  {"extend"};
    Map<String, String> filter;
    public ExtZabbixDtoGetTriggersInTroubleState(List<String> triggers) {
        filter = new HashMap<>();
        filter.put("value","1");
        this.triggerids = triggers;
    }
}
