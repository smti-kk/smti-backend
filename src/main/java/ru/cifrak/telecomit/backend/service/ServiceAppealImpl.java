package ru.cifrak.telecomit.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.dto.AppealCreateOrUpdateReq;
import ru.cifrak.telecomit.backend.entities.Appeal;
import ru.cifrak.telecomit.backend.entities.DBFile;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.RepositoryAppeal;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.util.Objects;

@Service
public class ServiceAppealImpl implements ServiceAppeal {
    private final RepositoryAppeal repositoryAppeal;
    private final RepositoryLocation repositoryLocation;
    private final DBFileStorageService dbFileStorageService;

    public ServiceAppealImpl(RepositoryAppeal repositoryAppeal,
                             RepositoryLocation repositoryLocation,
                             DBFileStorageService dbFileStorageService) {
        this.repositoryAppeal = repositoryAppeal;
        this.repositoryLocation = repositoryLocation;
        this.dbFileStorageService = dbFileStorageService;
    }

    @Override
    public Page<Appeal> findAll(Pageable pageable) {
        return repositoryAppeal.findAll(pageable);
    }

    @Override
    public Appeal getOne(Integer id) throws NotFoundException {
        return repositoryAppeal
                .findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void delete(Integer id) {
        repositoryAppeal.deleteById(id);
    }

    @Override
    @Transactional
    public Appeal updateOrCreate(AppealCreateOrUpdateReq appealCreateOrUpdateReq,
                                 MultipartFile file,
                                 MultipartFile responseFile) {
        Location location = repositoryLocation.get(appealCreateOrUpdateReq.getLocationId());
        DBFile appealFile = null;
        DBFile appealResponseFile = null;
        if (Objects.nonNull(file)) {
            appealFile = dbFileStorageService.storeFile(file);
        }
        if (Objects.nonNull(responseFile)) {
            appealResponseFile = dbFileStorageService.storeFile(responseFile);
        }
        Appeal appeal = new Appeal(
                appealCreateOrUpdateReq.getId(),
                appealCreateOrUpdateReq.getTitle(),
                appealCreateOrUpdateReq.getStatus(),
                appealCreateOrUpdateReq.getPriority(),
                appealCreateOrUpdateReq.getLevel(),
                location,
                appealCreateOrUpdateReq.getDate(),
                appealFile,
                appealResponseFile
        );
        return repositoryAppeal.save(appeal);
    }
}
