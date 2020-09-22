package ru.cifrak.telecomit.backend.serializer;


import ru.cifrak.telecomit.backend.entities.Signal;

import javax.persistence.AttributeConverter;

public class SignalOneConverter implements AttributeConverter<Signal, String> {
    @Override
    public String convertToDatabaseColumn(Signal signal) {
        return signal.toString();
    }

    @Override
    public Signal convertToEntityAttribute(String s) {
        if (s != null) {
            if (s.equals(Signal.ANLG.getId().toString())) {
                return Signal.ANLG;
            } else if (s.equals(Signal.DIGT.getId().toString())) {
                return Signal.DIGT;
            } else {
                return null;
            }
        }
        return null;
    }
}
