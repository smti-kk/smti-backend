package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.Appeal;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.service.ServiceAppeal;

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
    public Appeal updateOrCreate(Appeal appeal, User user) {
        log.info("User {} create appeal {} ->", user.getEmail(), appeal);
        Appeal newAppeal = serviceAppeal.updateOrCreate(appeal);
        log.info("User {} create appeal with id {} <-", user.getEmail(), appeal.getId());
        return newAppeal;
    }
}
