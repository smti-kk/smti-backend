package ru.cifrak.telecomit.backend.utils.export;

import java.util.function.Function;

public class ColumnConfig<T, D> {
    private final Integer columnNumber;
    private final Function toTypeMethod;
    private final Class clazz;
    private final String header;

    public ColumnConfig(int columnNumber, Class<? extends D> clazz, Function<T, D> toTypeMethod, String header) {
        this.header = header;
        this.toTypeMethod = toTypeMethod;
        this.columnNumber = columnNumber;
        this.clazz = clazz;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public Function getToTypeMethod() {
        return toTypeMethod;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getHeader() {
        return header;
    }
}

