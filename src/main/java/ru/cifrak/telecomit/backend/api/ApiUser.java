package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryAccount;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryUser;
import ru.cifrak.telecomit.backend.entities.Account;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/")
public class ApiUser {
    private final static String API_PATH = "/api/user/";
    private final RepositoryUser rUser;
    private final RepositoryAccount rAccount;

    public ApiUser(RepositoryUser userRepository, RepositoryAccount rAccount) {
        this.rUser = userRepository;
        this.rAccount = rAccount;
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    public List<Account> list(@AuthenticationPrincipal User user) {
        log.info("[{}]-> GET {}", user.getUsername(), API_PATH);
        List<Account> results = rAccount.findAllByUsernameIsNot("admin");
        log.info("[{}]<- GET {}", user.getUsername(), API_PATH);
        return results;
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Account> update(@RequestBody Account value, @AuthenticationPrincipal User user) {
        log.info("[{}]-> PUT {}::{}", user.getUsername(), API_PATH, value.getId());
        //TODO:[generate TICKET]: null check
        Account item = rAccount.findById(value.getId()).get();
        item.setEmail(value.getEmail());
        item.setFirstName(value.getFirstName());
        item.setLastName(value.getLastName());
        item.setPatronymicName(value.getPatronymicName());
        item.setLocations(value.getLocations());
        item.setOrganizations(value.getOrganizations());
        item.setIsActive(value.getIsActive());
        item.setRoles(value.getRoles());
        item = rAccount.save(item);
        log.info("[{}]<- PUT {}::{}", user.getUsername(), API_PATH, value.getId());
        return ResponseEntity.ok(item);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Account> create(@RequestBody Account value, @AuthenticationPrincipal User user) {
        log.info("[{}]-> POST {}", user.getUsername(), API_PATH);
        User item = new User();
        item.setUsername(value.getUsername());
        item.setEmail(value.getEmail());
        item.setFirstName(value.getFirstName());
        item.setLastName(value.getLastName());
        item.setPatronymicName(value.getPatronymicName());
        item.setIsActive(Boolean.TRUE);
        item.setCreateDateTime(LocalDateTime.now());
        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.GUEST);
        item.setRoles(roles);
        item = rUser.save(item);
        Account result = rAccount.findById(item.getId()).get();
        log.info("[{}]<- POST {}::{}", user.getUsername(), API_PATH, result.getId());
        return ResponseEntity.ok(result);
    }
}
