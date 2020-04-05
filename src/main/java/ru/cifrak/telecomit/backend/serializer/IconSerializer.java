package ru.cifrak.telecomit.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.cifrak.telecomit.backend.domain.MonitoringConnectionMetric;

import java.io.IOException;

public class IconSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String icon,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(String.format("/media/%s", icon));
    }
}
