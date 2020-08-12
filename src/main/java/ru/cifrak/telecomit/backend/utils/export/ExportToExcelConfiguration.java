package ru.cifrak.telecomit.backend.utils.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class ExportToExcelConfiguration<T> {
    private final Collection<ColumnConfig> columnConfigs;

    public ExportToExcelConfiguration() {
        columnConfigs = new ArrayList<>();
    }

    public <D> void addColumn(int columnIndex,  Class<? extends D> clazz, Function<T, D> toTypeMethod, String header) {
        columnConfigs.add(new ColumnConfig<>(columnIndex, clazz, toTypeMethod, header));
    }

    public void addColumn(int columnIndex, Function<T, String> toTypeMethod, String header) {
        columnConfigs.add(new ColumnConfig<>(columnIndex, String.class, toTypeMethod, header));
    }

    public Collection<ColumnConfig> getColumnConfigs() {
        return columnConfigs;
    }
}

