package ru.cifrak.telecomit.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.dto.AppealCreateOrUpdateReq;
import ru.cifrak.telecomit.backend.entities.Appeal;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

public interface ServiceAppeal {
    Page<Appeal> findAll(Pageable pageable);
    Appeal getOne(Integer id) throws NotFoundException;
    void delete(Integer id);
    Appeal updateOrCreate(AppealCreateOrUpdateReq appeal, MultipartFile file, MultipartFile responseFile);
}
