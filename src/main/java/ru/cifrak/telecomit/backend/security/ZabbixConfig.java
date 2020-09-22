package ru.cifrak.telecomit.backend.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "zabbix")
public class ZabbixConfig {
    private String host;
    private String login;
    private String password;
}
