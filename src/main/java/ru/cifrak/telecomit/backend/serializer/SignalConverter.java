package ru.cifrak.telecomit.backend.serializer;

import ru.cifrak.telecomit.backend.domain.Signal;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SignalConverter implements AttributeConverter<List<Signal>, String> {
    @Override
    public String convertToDatabaseColumn(List<Signal> signals) {
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
                        if (signalIdStr.equals(Signal.ATV.getId().toString())) {
                            return Signal.ATV;
                        } else if (signalIdStr.equals(Signal.CTV.getId().toString())) {
                            return Signal.CTV;
                        } else {
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }
}
