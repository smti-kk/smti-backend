package ru.cifrak.telecomit.backend.api.service.imp.ap;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.repository.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class ApesFromExcelDTOValidated implements ApesDTOFromExcel {

    private final RepositoryOrganization repositoryOrganization;

    private final RepositoryInternetAccessType repositoryInternetAccessType;

    private final RepositorySmoType repositorySmoType;

    private final RepositoryOrganizationType repositoryOrganizationType;

    private final RepositoryLocation repositoryLocation;

    private final ApesDTOFromExcel origin;

    public ApesFromExcelDTOValidated(
            RepositoryOrganization repositoryOrganization,
            RepositoryInternetAccessType repositoryInternetAccessType,
            RepositorySmoType repositorySmoType,
            RepositoryOrganizationType repositoryOrganizationType,
            RepositoryLocation repositoryLocation,
            ApesDTOFromExcel origin) {
        this.repositoryOrganization = repositoryOrganization;
        this.repositoryInternetAccessType = repositoryInternetAccessType;
        this.repositorySmoType = repositorySmoType;
        this.repositoryOrganizationType = repositoryOrganizationType;
        this.repositoryLocation = repositoryLocation;
        this.origin = origin;
    }

    @Override
    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    @Override
    public List<ApFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException {
        this.checkFormatFile(this.getFile());

        return this.checkTces(origin.getTcesDTO());
    }

    private List<ApFromExcelDTO> checkTces(List<ApFromExcelDTO> tcesDTO)
            throws FromExcelDTOFormatException {
        String badDTO;

        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTOFormatException("Не все \"№ п/п\" заполнены.");
        }

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO + " позиции не все ячейки заполнены.");
        }

        badDTO = this.checkFiasLocationGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС населённого пункта, должен быть в GUID формате.");
        }

        badDTO = this.checkFiasLocation(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС населённого пункта, не найден в БД.");
        }

        badDTO = this.checkFiasGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС организации, должен быть в GUID формате.");
        }

        badDTO = this.checkTypeInternetAccess(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в типе подключения, должен быть одним из {"
                    + String.join(", ", repositoryInternetAccessType.findAllTypes())
                    + "}.");
        }

        badDTO = this.checkTypeSmo(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в виде СЗО, должен быть одним из {"
                    + String.join(", ", repositorySmoType.findAllTypes())
                    + "}.");
        }

        badDTO = this.checkTypeOrganization(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в типе учреждения, должен быть одним из {"
                    + String.join(", ", repositoryOrganizationType.findAllTypes())
                    + "}.");
        }

        return tcesDTO;
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
