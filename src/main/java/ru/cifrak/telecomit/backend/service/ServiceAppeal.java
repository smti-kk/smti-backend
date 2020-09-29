package ru.cifrak.telecomit.backend.service;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.dto.AppealCreateOrUpdateReq;
import ru.cifrak.telecomit.backend.entities.Appeal;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.List;

public interface ServiceAppeal {
    List<Appeal> findAll();
    Appeal getOne(Integer id) throws NotFoundException;
    void delete(Integer id);
    Appeal updateOrCreate(AppealCreateOrUpdateReq appeal, MultipartFile file, MultipartFile responseFile);
}
