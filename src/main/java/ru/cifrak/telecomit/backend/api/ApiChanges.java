package ru.cifrak.telecomit.backend.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.ChangesDto;
import ru.cifrak.telecomit.backend.service.ServiceAccessPoint;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/type/changes")
@RequiredArgsConstructor
public class ApiChanges {

    private final ServiceAccessPoint sAccessPoint;

    @GetMapping("/list")
    public List<ChangesDto> getChangesList(
            @RequestParam(name = "apType", required = false) String apType
    ) {
        log.info("-> GET /api/type/changes/list");
        List<ChangesDto> result = sAccessPoint.getListChanges(apType);
        log.info("<- GET /api/type/changes/list");
        return result;
    }

}
