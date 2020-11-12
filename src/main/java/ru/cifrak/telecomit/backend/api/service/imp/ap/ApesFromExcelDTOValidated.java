package ru.cifrak.telecomit.backend.api.service.imp.ap;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOErrorException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTONppException;
import ru.cifrak.telecomit.backend.entities.TypeAccessPoint;
import ru.cifrak.telecomit.backend.repository.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ApesFromExcelDTOValidated {

    private final RepositoryOrganization repositoryOrganization;

    private final RepositoryInternetAccessType repositoryInternetAccessType;

    private final RepositorySmoType repositorySmoType;

    private final RepositoryOrganizationType repositoryOrganizationType;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram;

    private final ApesDTOFromExcel origin;

    public ApesFromExcelDTOValidated(
            RepositoryOrganization repositoryOrganization,
            RepositoryInternetAccessType repositoryInternetAccessType,
            RepositorySmoType repositorySmoType,
            RepositoryOrganizationType repositoryOrganizationType,
            RepositoryLocation repositoryLocation,
            RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram,
            ApesDTOFromExcel origin) {
        this.repositoryOrganization = repositoryOrganization;
        this.repositoryInternetAccessType = repositoryInternetAccessType;
        this.repositorySmoType = repositorySmoType;
        this.repositoryOrganizationType = repositoryOrganizationType;
        this.repositoryLocation = repositoryLocation;
        this.repositoryGovernmentDevelopmentProgram = repositoryGovernmentDevelopmentProgram;
        this.origin = origin;
    }

    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    private Workbook createErrorBook() {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Ошибки");
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("Позиция");
        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("Описание");
        return book;
    }

    private void addError(Sheet sheet, int nRow, String npp, String error) {
        Row row = sheet.createRow(nRow);
        Cell cell = row.createCell(0);
        cell.setCellValue(Integer.parseInt(npp));
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        row.createCell(1).setCellValue(error);
    }

    private ByteArrayResource errorBookToByteStream (Workbook book) throws IOException {
        Sheet sheet = book.getSheetAt(0);
        sheet.autoSizeColumn(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        book.write(baos);
        book.close();
        return new ByteArrayResource(baos.toByteArray());
    }

    public ApImportResult getTcesDTO() throws FromExcelDTOFormatException,
            FromExcelDTONppException, IOException {
        this.checkFormatFile(this.getFile());
        List<ApFromExcelDTO> tcesDTO = origin.getTcesDTO();
        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTONppException("Не все \"№ п/п\" заполнены.");
        }
        int importFailure = 0;
        int importSuccess = 0;
        List<ApFromExcelDTO> toImport = new ArrayList<>();
        List<ApFromExcelDTO> toCheck = new ArrayList<>();
        Workbook book = createErrorBook();
        Sheet sheet = book.getSheetAt(0);
        for (ApFromExcelDTO tcDTO : origin.getTcesDTO()) {
            toCheck.clear();
            toCheck.add(tcDTO);
            try {
                checkTces(toCheck);
                toImport.add(tcDTO);
                importSuccess++;
            } catch (FromExcelDTOErrorException e) {
                importFailure++;
                addError(sheet, importFailure, tcDTO.getNpp(), e.getMessage());
            }
        }
        return new ApImportResult(
                importSuccess,
                importFailure,
                errorBookToByteStream(book),
                toImport
        );
    }

    private void checkTces(List<ApFromExcelDTO> tcesDTO)
            throws FromExcelDTOErrorException {
        String badDTO;

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Не все ячейки заполнены.");
        }

        badDTO = this.checkFiasLocationGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС населённого пункта, должен быть в GUID формате.");
        }

        badDTO = this.checkFiasLocation(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС населённого пункта, не найден в БД.");
        }

        badDTO = this.checkFiasGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС организации, должен быть в GUID формате.");
        }

        badDTO = this.checkTypeInternetAccess(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в типе подключения, должен быть одним из {"
                    + String.join(", ", repositoryInternetAccessType.findAllTypes())
                    + "}.");
        }

        badDTO = this.checkTypeSmo(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в виде СЗО, должен быть одним из {"
                    + String.join(", ", repositorySmoType.findAllTypes())
                    + "}.");
        }

        badDTO = this.checkTypeOrganization(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в типе учреждения, должен быть одним из {"
                    + String.join(", ", repositoryOrganizationType.findAllTypes())
                    + "}.");
        }

        badDTO = this.checkProgram(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в программе, должна быть одной из {"
                    + String.join(", ", repositoryGovernmentDevelopmentProgram.findAllAcronym()) + "}.");
        }

        badDTO = this.checkTypeAccessPoint(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в типе точки подключения, должна быть одной из {"
                    + String.join(", ", getTypesAccessPoint())
                    + "}.");
        }
    }

    private List<String> getTypesAccessPoint() {
        TypeAccessPoint[] types = TypeAccessPoint.values();
        return Arrays.stream(types).map(TypeAccessPoint::getDesc).collect(Collectors.toList());
    }

    private String checkTypeOrganization(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        for (ApFromExcelDTO tcDTO : tcesDTO) {
            if (repositoryOrganizationType.findByName(tcDTO.getType()) == null) {
                result = tcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkProgram(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        // TODO: List<String> -> List<GovernmentDevelopmentProgram>.
        List<String> programs = repositoryGovernmentDevelopmentProgram.findAllAcronym();
        for (ApFromExcelDTO tcDTO : tcesDTO) {
            if (!tcDTO.getProgram().isEmpty() && !programs.contains(tcDTO.getProgram())) {
                result = tcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkTypeSmo(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        for (ApFromExcelDTO tcDTO : tcesDTO) {
            if (!tcDTO.getSmo().isEmpty() &&
                    repositorySmoType.findByName(tcDTO.getSmo()) == null) {
                result = tcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkTypeInternetAccess(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        for (ApFromExcelDTO TcDTO : tcesDTO) {
            if (repositoryInternetAccessType.findByName(TcDTO.getTypeInternetAccess()) == null) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkTypeAccessPoint(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        for (ApFromExcelDTO TcDTO : tcesDTO) {
            if (!getTypesAccessPoint().contains(TcDTO.getTypeAccessPoint())) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private void checkFormatFile(MultipartFile file) throws FromExcelDTOFormatException {
        boolean result;
        InputStream is = this.checkIOFile(file);
        if (is == null) {
            result = false;
        } else {
            result = this.checkExcelFormat(is);
        }
        if (!result) {
            throw new FromExcelDTOFormatException("Неправильный тип файла.");
        }
    }

    private InputStream checkIOFile(MultipartFile file) {
        InputStream result;
        try {
            result = file.getInputStream();
        } catch (IOException e) {
            result = null;
        }
        return result;
    }

    private boolean checkExcelFormat(InputStream is) {
        boolean result = true;
        try {
            new XSSFWorkbook(is);
        } catch (Exception eXSSF) {
//            try {
//                new HSSFWorkbook(is);
//            } catch (Exception eHSSF) {
                result = false;
//            }
        }
        return result;
    }

    private boolean checkFullnessNpp(List<ApFromExcelDTO> tcesDTO) {
        boolean result = true;
        for (ApFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        for (ApFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getFiasLocation().isEmpty()
                    || TcDTO.getName().isEmpty()
                    || TcDTO.getAddress().isEmpty()
                    || TcDTO.getLatitude().isEmpty()
                    || TcDTO.getLongitude().isEmpty()
                    || TcDTO.getFias().isEmpty()
                    || TcDTO.getType().isEmpty()
                    || TcDTO.getContractor().isEmpty()
                    || TcDTO.getTypeInternetAccess().isEmpty()
                    || TcDTO.getDeclaredSpeed().isEmpty()
                    || TcDTO.getTypeAccessPoint().isEmpty()
            ) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasGUID(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        for (ApFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasLocationGUID(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        for (ApFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getFiasLocation()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasLocation(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        for (ApFromExcelDTO tcDTO : tcesDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(tcDTO.getFiasLocation())) == null) {
                result = tcDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
