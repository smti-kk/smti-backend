package ru.cifrak.telecomit.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.UserRole;
import ru.cifrak.telecomit.backend.auth.service.UserService;
import ru.cifrak.telecomit.backend.cache.repository.AuthTokenCacheRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
//@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EntityScan
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @EventListener
    public void doAfterAppStart(ApplicationReadyEvent event) {
        final ConfigurableApplicationContext context = event.getApplicationContext();
        BackendApplication.addAdminUser(context);
        BackendApplication.resetAuthTokenCache(context);
    }

    public static void addAdminUser(ApplicationContext context) {
        final ZoneId zoneId = ZoneId.systemDefault(); // TODO get from properties
        final LocalDateTime nowTime = LocalDateTime.now(zoneId);

        final PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        final UserService userService = context.getBean(UserService.class);
        final Optional<User> optionalUser = userService.findByUsername("admin");

        if (optionalUser.isPresent()) {
            return;
        }

        final User newUser = new User();
        newUser.setUsername("admin");
        newUser.setFirstName("admin");
        newUser.setPassword(passwordEncoder.encode("pwd"));
        newUser.getRoles().add(UserRole.ADMIN);
        newUser.getRoles().add(UserRole.OPERATOR);
        newUser.setCreateDateTime(nowTime);
        userService.save(newUser);
        log.info("user admin created with default password");
    }

    public static void resetAuthTokenCache(ApplicationContext context) {
        final AuthTokenCacheRepository authTokenCacheRepository = context.getBean(AuthTokenCacheRepository.class);
        authTokenCacheRepository.deleteAll();
        log.info("AuthTokenCache clean");
    }

}
