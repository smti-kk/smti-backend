package ru.cifrak.telecomit.backend.service;

import lombok.SneakyThrows;
import ru.cifrak.telecomit.backend.entities.ExcelExportTypes;
import ru.cifrak.telecomit.backend.entities.TcType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportName {

    private final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

    private final ExcelExportTypes type;

    public ReportName(ExcelExportTypes type) {
        this.type = type;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return "attachment; " +  "filename=\"" + URLEncoder.encode(date.format(Calendar.getInstance().getTime()) + "_" + type.toString() + "_отчет", StandardCharsets.UTF_8.toString()) + ".xlsx\"";
    }
}
