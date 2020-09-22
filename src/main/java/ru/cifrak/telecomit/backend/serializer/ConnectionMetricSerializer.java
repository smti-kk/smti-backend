/*
package ru.cifrak.telecomit.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.cifrak.telecomit.backend.domain.MonitoringConnectionMetric;

import java.io.IOException;

public class ConnectionMetricSerializer extends JsonSerializer<MonitoringConnectionMetric> {
    @Override
    public void serialize(MonitoringConnectionMetric monitoringConnectionMetric,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("access_point", monitoringConnectionMetric.getMonitoringAccesspointRe().getId());
        jsonGenerator.writeBooleanField("available", monitoringConnectionMetric.getAvailable());
        jsonGenerator.writeNumberField("id", monitoringConnectionMetric.getId());
        jsonGenerator.writeEndObject();
    }
}
*/
