package ru.cifrak.telecomit.backend.api.service.imp.location;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LocationsFromExcelDTOValidated implements LocationsDTOFromExcel {

    private final RepositoryLocation repository;

    private final LocationsDTOFromExcel origin;

    public LocationsFromExcelDTOValidated(RepositoryLocation repository, LocationsDTOFromExcel origin) {
        this.repository = repository;
        this.origin = origin;
    }

    @Override
    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    @Override
    public List<LocationFromExcelDTO> getLocationsDTO() throws FromExcelDTOFormatException {
        this.checkFormatFile(this.getFile());

        return this.checkLocations(origin.getLocationsDTO());
    }

    private List<LocationFromExcelDTO> checkLocations(List<LocationFromExcelDTO> locationsDTO)
            throws FromExcelDTOFormatException {
        String badLocationDTO;

        if (!this.checkFullnessNpp(locationsDTO)) {
            throw new FromExcelDTOFormatException("Not all npp are filled.");
        }

        badLocationDTO = this.checkFullnessCells(locationsDTO);
        if (badLocationDTO != null) {
            throw new FromExcelDTOFormatException("In " + badLocationDTO + " position not all cells are filled.");
        }

        badLocationDTO = this.checkFiases(locationsDTO);
        if (badLocationDTO != null) {
            throw new FromExcelDTOFormatException("In " + badLocationDTO
                    + " position FIAS error, must be in GUID-format.");
        }

        badLocationDTO = this.checkTypes(locationsDTO);
        if (badLocationDTO != null) {
            throw new FromExcelDTOFormatException("In " + badLocationDTO
                    + " position type error, must be in {"
                    + String.join(", ", repository.findAllTypes()) + "}.");
        }

        badLocationDTO = this.checkPopulation(locationsDTO);
        if (badLocationDTO != null) {
            throw new FromExcelDTOFormatException("In " + badLocationDTO
                    + " position population format error, must be in numeric format.");
        }

        return locationsDTO;
    }

    private String checkPopulation(List<LocationFromExcelDTO> locationsDTO) {
        String result = null;
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (!locationDTO.getPopulation().matches("[0-9]+")) {
                result = locationDTO.getNpp();
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

    private boolean checkFullnessNpp(List<LocationFromExcelDTO> locationsDTO) {
        boolean result = true;
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (locationDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<LocationFromExcelDTO> locationsDTO) {
        String result = null;
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (locationDTO.getTypeMO().isEmpty()
                    || locationDTO.getNameMO().isEmpty()
                    || locationDTO.getType().isEmpty()
                    || locationDTO.getName().isEmpty()
                    || locationDTO.getFias().isEmpty()
                    || locationDTO.getPopulation().isEmpty()
            ) {
                result = locationDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiases(List<LocationFromExcelDTO> locationsDTO) {
        String result = null;
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (!locationDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = locationDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkTypes(List<LocationFromExcelDTO> locationsDTO) {
        String result = null;
        // TODO: List<String> -> List<Locations>
        List<String> typesOfLocationsDTO = repository.findAllTypes();
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (!typesOfLocationsDTO.contains(locationDTO.getType())
                    || !typesOfLocationsDTO.contains(locationDTO.getTypeMO())) {
                result = locationDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
