package ru.cifrak.telecomit.backend.api;

import org.apache.poi.util.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.DBFile;
import ru.cifrak.telecomit.backend.service.DBFileStorageService;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/api/downloadFile")
public class ApiFiles {
    private final DBFileStorageService dbFileStorageService;

    public ApiFiles(DBFileStorageService dbFileStorageService) {
        this.dbFileStorageService = dbFileStorageService;
    }

    @GetMapping("{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        DBFile dbFile;
        dbFile = dbFileStorageService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @GetMapping("/logs")
    public ResponseEntity<Resource> downloadLogFile() throws IOException {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("static/logs.log");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "logs" + "\"")
                .body(new ByteArrayResource(IOUtils.toByteArray(in)));
    }
}
