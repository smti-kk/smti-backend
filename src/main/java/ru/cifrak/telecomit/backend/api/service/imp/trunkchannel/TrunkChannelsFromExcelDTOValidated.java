package ru.cifrak.telecomit.backend.api.service.imp.trunkchannel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.repository.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class TrunkChannelsFromExcelDTOValidated implements TrunkChannelsDTOFromExcel {

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;

    private final TrunkChannelsDTOFromExcel origin;

    public TrunkChannelsFromExcelDTOValidated(
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel,
            TrunkChannelsDTOFromExcel origin) {
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
        this.origin = origin;
    }

    @Override
    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    @Override
    public List<TrunkChannelFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException {
        this.checkFormatFile(this.getFile());

        return this.checkTces(origin.getTcesDTO());
    }

    private List<TrunkChannelFromExcelDTO> checkTces(List<TrunkChannelFromExcelDTO> tcesDTO)
            throws FromExcelDTOFormatException {
        String badDTO;

        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTOFormatException("Не все \"№ п/п\" заполнены.");
        }

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO + " позиции не все ячейки заполнены.");
        }

        badDTO = this.checkLocationStartGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС начального населённого пункта, должен быть в GUID формате.");
        }

        badDTO = this.checkLocationStart(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС начального населённого пункта, не найден в БД.");
        }

        badDTO = this.checkLocationEndGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС конечного населённого пункта, должен быть в GUID формате.");
        }

        badDTO = this.checkLocationEnd(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС конечного населённого пункта, не найден в БД.");
        }

        badDTO = this.checkOperators(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в операторе, не найден в БД.");
        }

        badDTO = this.checkChannel(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в типе канала, не найден в БД.");
        }

        return tcesDTO;
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
}
