package ru.cifrak.telecomit.backend.api.service.imp.tcinternet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryTypeTruncChannel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TcesInternetFromExcelDTOValidated implements TcesInternetDTOFromExcel {

    private final RepositoryOperator repositoryOperator;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;

    private final TcesInternetDTOFromExcel origin;

    public TcesInternetFromExcelDTOValidated(
            RepositoryOperator repositoryOperator,
            RepositoryLocation repositoryLocation,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel,
            TcesInternetDTOFromExcel origin) {
        this.repositoryOperator = repositoryOperator;
        this.repositoryLocation = repositoryLocation;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
        this.origin = origin;
    }

    @Override
    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    @Override
    public List<TcInternetFromExcelDTO> getTcesInternetDTO() throws FromExcelDTOFormatException {
        this.checkFormatFile(this.getFile());

        return this.checkTcesInternet(origin.getTcesInternetDTO());
    }

    private List<TcInternetFromExcelDTO> checkTcesInternet(List<TcInternetFromExcelDTO> tcesInternetDTO)
            throws FromExcelDTOFormatException {
        String badTcInternetDTO;

        if (!this.checkFullnessNpp(tcesInternetDTO)) {
            throw new FromExcelDTOFormatException("Не все \"№ п/п\" заполнены.");
        }

        badTcInternetDTO = this.checkFullnessCells(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOFormatException("В " + badTcInternetDTO + " позиции не все ячейки заполнены.");
        }

        badTcInternetDTO = this.checkFiasesGUID(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOFormatException("В " + badTcInternetDTO
                    + " позиции ошибка в ФИАС, должен быть в GUID формате.");
        }

        badTcInternetDTO = this.checkFiases(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOFormatException("В " + badTcInternetDTO
                    + " позиции ошибка в ФИАС населённого пункта, не найден в БД.");
        }

        badTcInternetDTO = this.checkOperators(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOFormatException("В " + badTcInternetDTO
                    + " позиции ошибка в операторе, не найден в БД.");
        }

        badTcInternetDTO = this.checkOperatorsRights(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOFormatException("В " + badTcInternetDTO
                    + " позиции ошибка в операторе, данную Т/В могут предоставлять только {"
                    + repositoryOperator.internet()
                    .stream().map(Operator::getName).collect(Collectors.joining(", "))
                    + "}.");
        }

        badTcInternetDTO = this.checkChannel(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOFormatException("В " + badTcInternetDTO
                    + " позиции ошибка в типе канала, не найден в БД.");
        }

        return tcesInternetDTO;
    }

    private String checkChannel(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (repositoryTypeTruncChannel.findByName(TcInternetDTO.getChannel()) == null) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiases(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(TcInternetDTO.getFias())) == null) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperators(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (repositoryOperator.findByName(TcInternetDTO.getOperator()) == null) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperatorsRights(List<TcInternetFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcDTO : tcesDTO) {
            if (!repositoryOperator.internet().contains(repositoryOperator.findByName(TcDTO.getOperator()))) {
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

    private boolean checkFullnessNpp(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        boolean result = true;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (TcInternetDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (TcInternetDTO.getFias().isEmpty()
                    || TcInternetDTO.getOperator().isEmpty()
                    || TcInternetDTO.getChannel().isEmpty()
            ) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasesGUID(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (!TcInternetDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
