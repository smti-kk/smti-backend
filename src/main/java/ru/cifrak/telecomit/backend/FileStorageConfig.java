package ru.cifrak.telecomit.backend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageConfig {
    private String path;
    private String tmp;
    private Integer maxFiles;
    private Integer maxSize;
    private String acceptedFileTypes;
}
