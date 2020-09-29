package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.dto.AppealCreateOrUpdateReq;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.service.ServiceAppeal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
public class ApiAppealImpl implements ApiAppeal {
    private final ServiceAppeal serviceAppeal;

    public ApiAppealImpl(ServiceAppeal serviceAppeal) {
        this.serviceAppeal = serviceAppeal;
    }

    @Override
    public List<Appeal> findAll(User user) {
        log.info("User {} request all appeals ->", user.getEmail());
        List<Appeal> result = serviceAppeal.findAll();
        log.info("User {} request all appeals <-", user.getEmail());
        return result;
    }

    @Override
    public Appeal getOne(Integer id, User user) {
        log.info("User {} request appeal with id {} ->", user.getEmail(), id);
        try {
            Appeal result = serviceAppeal.getOne(id);
            log.info("User {} request appeal with id {} <-", user.getEmail(), id);
            return result;
        } catch (NotFoundException exception) {
            log.error("User {} request appeal with id {} throw not found", user.getEmail(), id);
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(Integer id, User user) {
        log.info("User {} delete appeal with id {} ->", user.getEmail(), id);
        serviceAppeal.delete(id);
        log.info("User {} delete appeal with id {} <-", user.getEmail(), id);
    }

    @Override
    public Appeal updateOrCreate(Integer id,
                                 String title,
                                 AppealStatus status,
                                 AppealPriority priority,
                                 AppealLevel level,
                                 Integer locationId,
                                 LocalDate date,
                                 MultipartFile file,
                                 MultipartFile responseFile,
                                 User user) {
        AppealCreateOrUpdateReq appeal = new AppealCreateOrUpdateReq(
                id,
                title,
                status,
                priority,
                level,
                locationId,
                date
        );
        log.info("User {} create or update appeal {} ->", user.getEmail(), appeal);
        Appeal newAppeal = serviceAppeal.updateOrCreate(appeal, file, responseFile);
        log.info("User {} create appeal with id {} <-", user.getEmail(), appeal.getId());
        return newAppeal;
    }
}
