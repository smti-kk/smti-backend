package ru.cifrak.telecomit.backend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.cifrak.telecomit.backend.entities.User;

import java.util.Optional;

@Slf4j

@Configuration
public class JPAConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
//        log.info("-> auditor aware");
//        return () -> Optional.of(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return () -> Optional.of("admin");
//        return () -> Optional.of(((User)     SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }
}
