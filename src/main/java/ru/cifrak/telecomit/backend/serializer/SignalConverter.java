package ru.cifrak.telecomit.backend.serializer;


import ru.cifrak.telecomit.backend.entities.Signal;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SignalConverter implements AttributeConverter<List<Signal>, String> {
    @Override
    public String convertToDatabaseColumn(List<Signal> signals) {
        if (signals == null || signals.isEmpty()) {
            return null;
        }
        return signals.stream()
                .map(s -> s.getId().toString())
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Signal> convertToEntityAttribute(String s) {
        if (s != null) {
            return Arrays
                    .stream(s.split(","))
                    .map(signalIdStr -> {
                        if (signalIdStr.equals(Signal.ANLG.getId().toString())) {
                            return Signal.ANLG;
                        } else if (signalIdStr.equals(Signal.DIGT.getId().toString())) {
                            return Signal.DIGT;
                        } else {
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
