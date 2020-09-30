package ru.cifrak.telecomit.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.entities.DBFile;
import ru.cifrak.telecomit.backend.repository.DBFileRepository;

import java.io.IOException;

@Service
public class DBFileStorageService {

    private final DBFileRepository dbFileRepository;

    public DBFileStorageService(DBFileRepository dbFileRepository) {
        this.dbFileRepository = dbFileRepository;
    }

    public DBFile storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public DBFile getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + fileId));
    }
}