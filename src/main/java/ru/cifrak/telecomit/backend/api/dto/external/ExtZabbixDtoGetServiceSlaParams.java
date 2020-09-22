package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class ExtZabbixDtoGetServiceSlaParams {
    List<String> serviceids;
    Object[] intervals;

    public ExtZabbixDtoGetServiceSlaParams(List<MonitoringAccessPoint> maps, Long start, Long end) {
        serviceids = maps.stream().map(i -> i.getServiceId().toString()).collect(Collectors.toList());
        intervals = new Object[1];
        intervals[0] = new Period(start, end);
    }

    @Value
    class Period {
        final Long from;
        final Long to;

        Period(Long from, Long to) {
            this.from = from;
            this.to = to;
        }
    }
}
