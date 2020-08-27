package ru.cifrak.telecomit.backend.api.service.imp.tcpost;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.entities.TypePost;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class TcesPostFromExcelDTOValidated implements TcesPostDTOFromExcel {

    private final RepositoryOperator repositoryOperator;

    private final RepositoryLocation repositoryLocation;

    private final TcesPostDTOFromExcel origin;

    public TcesPostFromExcelDTOValidated(
            RepositoryOperator repositoryOperator,
            RepositoryLocation repositoryLocation,
            TcesPostDTOFromExcel origin) {
        this.repositoryOperator = repositoryOperator;
        this.repositoryLocation = repositoryLocation;
        this.origin = origin;
    }

    @Override
    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    @Override
    public List<TcPostFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException {
        this.checkFormatFile(this.getFile());

        return this.checkTces(origin.getTcesDTO());
    }

    private List<TcPostFromExcelDTO> checkTces(List<TcPostFromExcelDTO> tcesDTO)
            throws FromExcelDTOFormatException {
        String badDTO;

        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTOFormatException("Не все \"№ п/п\" заполнены.");
        }

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO + " позиции не все ячейки заполнены.");
        }

        badDTO = this.checkFiasesGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС, должен быть в GUID формате.");
        }

        badDTO = this.checkFiases(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС населённого пункта, не найден в БД.");
        }

        badDTO = this.checkOperators(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в операторе, не найден в БД.");
        }

        badDTO = this.checkTypePost(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в типе, должен быть одним из {" + TypePost.UPS.getName() + ", " + TypePost.POST.getName() +"}.");
        }

        return tcesDTO;
    }

    private String checkTypePost(List<TcPostFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPostFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getTypePost().equals(TypePost.UPS.getName())
                    && !TcDTO.getTypePost().equals(TypePost.POST.getName())) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiases(List<TcPostFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPostFromExcelDTO TcDTO : tcesDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(TcDTO.getFias())) == null) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperators(List<TcPostFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPostFromExcelDTO TcDTO : tcesDTO) {
            if (repositoryOperator.findByName(TcDTO.getOperator()) == null) {
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

    private boolean checkFullnessNpp(List<TcPostFromExcelDTO> tcesDTO) {
        boolean result = true;
        for (TcPostFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<TcPostFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPostFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getFias().isEmpty()
                    || TcDTO.getOperator().isEmpty()
                    || TcDTO.getTypePost().isEmpty()
            ) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasesGUID(List<TcPostFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPostFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
