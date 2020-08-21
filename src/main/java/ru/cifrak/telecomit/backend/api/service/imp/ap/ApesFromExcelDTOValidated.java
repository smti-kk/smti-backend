package ru.cifrak.telecomit.backend.api.service.imp.ap;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.repository.RepositoryInternetAccessType;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ApesFromExcelDTOValidated implements ApesDTOFromExcel {

    private final RepositoryOrganization repositoryOrganization;

    private final RepositoryInternetAccessType repositoryInternetAccessType;

    private final ApesDTOFromExcel origin;

    public ApesFromExcelDTOValidated(
            RepositoryOrganization repositoryOrganization,
            RepositoryInternetAccessType repositoryInternetAccessType,
            ApesDTOFromExcel origin) {
        this.repositoryOrganization = repositoryOrganization;
        this.repositoryInternetAccessType = repositoryInternetAccessType;
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
            throw new FromExcelDTOFormatException("Not all npp are filled.");
        }

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("In " + badDTO + " position not all cells are filled.");
        }

        badDTO = this.checkFiaseGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("In " + badDTO
                    + " position organization FIAS error, must be in GUID-format.");
        }

        badDTO = this.checkTypeInternetAccess(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("In " + badDTO
                    + " position type internet access error, must be in {"
                    + String.join(", ", repositoryInternetAccessType.findAllTypes())
                    + "}.");
        }

        return tcesDTO;
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
            throw new FromExcelDTOFormatException("Wrong file type.");
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
            if (TcDTO.getLatitude().isEmpty()
                    || TcDTO.getLongitude().isEmpty()
                    || TcDTO.getOrganization().isEmpty()
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

    private String checkFiaseGUID(List<ApFromExcelDTO> tcesDTO) {
        String result = null;
        for (ApFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getOrganization()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
