package ru.cifrak.telecomit.backend.serializer;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerSignalConverter implements AttributeConverter<List<Integer>, String> {
    @Override
    public String convertToDatabaseColumn(List<Integer> integers) {
        if (integers == null) {
            return null;
        }
        return integers.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Integer> convertToEntityAttribute(@Nullable String s) {
        if (s == null) {
            return null;
        }
        return Arrays.stream(s.split(","))
                .map(Integer::new)
                .collect(Collectors.toList());
    }
}
