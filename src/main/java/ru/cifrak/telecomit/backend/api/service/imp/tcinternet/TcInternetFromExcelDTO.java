package ru.cifrak.telecomit.backend.api.service.imp.tcinternet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import ru.cifrak.telecomit.backend.entities.Location;

@AllArgsConstructor
@Getter
public class TcInternetFromExcelDTO {

    private final String npp;

    private final String typeMO;

    private final String nameMO;

    private final String fias;

    private final String name;

    private final String population;

    private final String type;

    public TcInternetFromExcelDTO(Row row) {
        this(
                row.getCell(0).getStringCellValue().trim(),
                row.getCell(1).getStringCellValue().trim().toLowerCase(),
                row.getCell(2).getStringCellValue().trim(),
                row.getCell(5).getStringCellValue().trim(),
                row.getCell(4).getStringCellValue().trim(),
                row.getCell(6).getStringCellValue().trim(),
                row.getCell(3).getStringCellValue().trim().toLowerCase()
        );
    }

    public boolean needUpdateName(Location location) {
        return !this.getName().trim().isEmpty() && !location.getName().equals(this.getName());
    }

    public boolean needUpdatePopulation(Location location) {
        return Integer.parseInt(this.getPopulation()) != 0
                && !location.getPopulation().equals(Integer.parseInt(this.getPopulation()));
    }

    public boolean needUpdateType(Location location) {
        return !this.getType().trim().isEmpty() && !location.getType().equals(this.getType());
    }
}
