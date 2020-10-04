package ru.cifrak.telecomit.backend.api.service.imp.trunkchannel;

import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOErrorException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTONppException;
import ru.cifrak.telecomit.backend.repository.*;

import java.awt.print.Book;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class TrunkChannelsFromExcelDTOValidated {

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;

    private final TrunkChannelsDTOFromExcel origin;

    private final RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram;

    public TrunkChannelsFromExcelDTOValidated(
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel,
            TrunkChannelsDTOFromExcel origin,
            RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram) {
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
        this.origin = origin;
        this.repositoryGovernmentDevelopmentProgram = repositoryGovernmentDevelopmentProgram;
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
        row.createCell(0).setCellValue(npp);
        row.createCell(1).setCellValue(error);
    }

    private ByteArrayResource errorBookToByteStream (Workbook book) {
        Sheet sheet = book.getSheetAt(0);
        sheet.autoSizeColumn(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        book.write(baos);
        book.close();
        new ByteArrayResource(baos.toByteArray())

    }

    public ImportResultTrunkChannel getTcesDTO() throws FromExcelDTOFormatException, FromExcelDTONppException, IOException {
        this.checkFormatFile(this.getFile());
        List<TrunkChannelFromExcelDTO> tcesDTO = origin.getTcesDTO();
        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTONppException("Не все \"№ п/п\" заполнены.");
        }
        int importFailure = 0;
        int importSuccess = 0;
        List<TrunkChannelFromExcelDTO> toImport = new ArrayList<>();
        List<TrunkChannelFromExcelDTO> toCheck = new ArrayList<>();
        Workbook book = createErrorBook();
        Sheet sheet = book.getSheetAt(0);
        for (TrunkChannelFromExcelDTO tcDTO : origin.getTcesDTO()) {
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
        return new ImportResultTrunkChannel(
                importSuccess,
                importFailure,
                errorSheetToByteStream(sheet),
                toImport
        );
    }

    private void checkTces(List<TrunkChannelFromExcelDTO> tcesDTO)
            throws FromExcelDTOErrorException {
        String badDTO;

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO + " позиции не все ячейки заполнены.");
        }

        badDTO = this.checkLocationStartGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO
                    + " позиции ошибка в ФИАС начального населённого пункта, должен быть в GUID формате.");
        }

        badDTO = this.checkLocationStart(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO
                    + " позиции ошибка в ФИАС начального населённого пункта, не найден в БД.");
        }

        badDTO = this.checkLocationEndGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO
                    + " позиции ошибка в ФИАС конечного населённого пункта, должен быть в GUID формате.");
        }

        badDTO = this.checkLocationEnd(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO
                    + " позиции ошибка в ФИАС конечного населённого пункта, не найден в БД.");
        }

        badDTO = this.checkCommissioningFormat(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO
                    + " позиции ошибка в дате ввода в эксплуатацию (" + badDTO + "), должна быть в формате ДД.ММ.ГГГГ.");
        }

        badDTO = this.checkCommissioningDate(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO
                    + " позиции ошибка в дате ввода в эксплуатацию, она должна быть реальной.");
        }

        badDTO = this.checkOperators(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO
                    + " позиции ошибка в операторе, не найден в БД.");
        }

        badDTO = this.checkChannel(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO
                    + " позиции ошибка в типе канала, не найден в БД.");
        }

        badDTO = this.checkProgram(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("В " + badDTO
                    + " позиции ошибка в программе, должна быть одной из {"
                    + String.join(", ", repositoryGovernmentDevelopmentProgram.findAllAcronym()) + "}.");
        }
    }

    private String checkChannel(List<TrunkChannelFromExcelDTO> tces) {
        String result = null;
        for (TrunkChannelFromExcelDTO tc : tces) {
            if (repositoryTypeTruncChannel.findByName(tc.getType()) == null) {
                result = tc.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperators(List<TrunkChannelFromExcelDTO> tces) {
        String result = null;
        for (TrunkChannelFromExcelDTO tc : tces) {
            if (repositoryOperator.findByName(tc.getOperator()) == null) {
                result = tc.getNpp();
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

    private boolean checkFullnessNpp(List<TrunkChannelFromExcelDTO> tcesDTO) {
        boolean result = true;
        for (TrunkChannelFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<TrunkChannelFromExcelDTO> tcesDTO) {
        String result = null;
        for (TrunkChannelFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getLocationStart().isEmpty()
                    || TcDTO.getLocationEnd().isEmpty()
                    || TcDTO.getOperator().isEmpty()
                    || TcDTO.getType().isEmpty()
            ) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkLocationStartGUID(List<TrunkChannelFromExcelDTO> tcesDTO) {
        String result = null;
        for (TrunkChannelFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getLocationStart()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkLocationEndGUID(List<TrunkChannelFromExcelDTO> tcesDTO) {
        String result = null;
        for (TrunkChannelFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getLocationEnd()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkLocationStart(List<TrunkChannelFromExcelDTO> tcesDTO) {
        String result = null;
        for (TrunkChannelFromExcelDTO tcDTO : tcesDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(tcDTO.getLocationStart())) == null) {
                result = tcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkLocationEnd(List<TrunkChannelFromExcelDTO> tcesDTO) {
        String result = null;
        for (TrunkChannelFromExcelDTO tcDTO : tcesDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(tcDTO.getLocationEnd())) == null) {
                result = tcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkProgram(List<TrunkChannelFromExcelDTO> tcesDTO) {
        String result = null;
        // TODO: List<String> -> List<GovernmentDevelopmentProgram>.
        List<String> programs = repositoryGovernmentDevelopmentProgram.findAllAcronym();
        for (TrunkChannelFromExcelDTO tcDTO : tcesDTO) {
            if (!tcDTO.getProgram().isEmpty() && !programs.contains(tcDTO.getProgram())) {
                result = tcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkCommissioningFormat(List<TrunkChannelFromExcelDTO> tcesDTO) {
        String result = null;
        for (TrunkChannelFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getComissioning().isEmpty()
                    && !TcDTO.getComissioning().matches("[0-9]{2}.[0-9]{2}.[0-9]{4}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkCommissioningDate(List<TrunkChannelFromExcelDTO> tcesDTO) {
        String result = null;
        for (TrunkChannelFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getComissioning().isEmpty()) {
                continue;
            }
            int yyyy = Integer.parseInt(TcDTO.getComissioning().substring(6, 10));
            int mm = Integer.parseInt(TcDTO.getComissioning().substring(3, 5)) - 1;
            int dd = Integer.parseInt(TcDTO.getComissioning().substring(0, 2));
            GregorianCalendar dateBefore50Years = new GregorianCalendar();
            dateBefore50Years.add(Calendar.YEAR, -50);
            GregorianCalendar dateAfter50Years = new GregorianCalendar();
            dateAfter50Years.add(Calendar.YEAR, +50);
            GregorianCalendar checkedDate = new GregorianCalendar(yyyy, mm, dd);
            if (yyyy != checkedDate.get(Calendar.YEAR)
                    || mm != checkedDate.get(Calendar.MONTH)
                    || dd != checkedDate.get(Calendar.DAY_OF_MONTH)
                    || checkedDate.before(dateBefore50Years)
                    || checkedDate.after(dateAfter50Years)) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
