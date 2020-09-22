package ru.cifrak.telecomit.backend.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "utm5")
public class UTM5Config {
    private String host;
    private String login;
    private String password;
    private String userpwd;
}
