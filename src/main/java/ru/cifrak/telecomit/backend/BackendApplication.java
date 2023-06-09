package ru.cifrak.telecomit.backend;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.cifrak.telecomit.backend.auth.service.UserService;
import ru.cifrak.telecomit.backend.cache.repository.AuthTokenCacheRepository;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.UserRole;
import ru.cifrak.telecomit.backend.service.LocationService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
//@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EntityScan
@EnableCaching
public class BackendApplication {

    // todo: разобраться с кэшем, как его правильно использовать, потом это удалить
    // требуется чтобы при перезапуске приложения сбросить кэш связанный с локациями
    private final LocationService locationService;

    public BackendApplication(LocationService locationService) {
        this.locationService = locationService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @EventListener
    public void doAfterAppStart(ApplicationReadyEvent event) {
        final ConfigurableApplicationContext context = event.getApplicationContext();
        BackendApplication.addAdminUser(context);
        locationService.refreshCache();
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
        newUser.setEmail("admin@admin.ru");
        newUser.setFirstName("admin");
        newUser.setPassword(passwordEncoder.encode("RjymCjkywtNhfdf"));
        newUser.getRoles().add(UserRole.ADMIN);
        newUser.setCreateDateTime(nowTime);
        userService.save(newUser);
        log.info("user admin created with default password");
    }

    public static void resetAuthTokenCache(ApplicationContext context) {
        final AuthTokenCacheRepository authTokenCacheRepository = context.getBean(AuthTokenCacheRepository.class);
        authTokenCacheRepository.deleteAll();
        log.info("AuthTokenCache clean");
    }

    //TODO: explore and control this module, how it works
    @Bean
    public Module configureObjectMapper() {
        return new Hibernate5Module();
    }
}
