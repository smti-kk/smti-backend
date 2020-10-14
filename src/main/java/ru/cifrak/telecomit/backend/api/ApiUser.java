package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryAccount;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryUser;
import ru.cifrak.telecomit.backend.entities.Account;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.UserRole;
import ru.cifrak.telecomit.backend.exceptions.JPAException;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/user/")
public class ApiUser {
    private final static String API_PATH = "/api/user/";
    private final RepositoryUser rUser;
    private final RepositoryAccount rAccount;
    private final RepositoryLocation rLocation;
    private final RepositoryOrganization rOrganization;
    private final PasswordEncoder passwordEncoder;

    public ApiUser(RepositoryUser userRepository, RepositoryAccount rAccount, RepositoryLocation rLocation, RepositoryOrganization rOrganization, PasswordEncoder passwordEncoder) {
        this.rUser = userRepository;
        this.rAccount = rAccount;
        this.rLocation = rLocation;
        this.rOrganization = rOrganization;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    public Page<Account> list(Pageable pageable, @AuthenticationPrincipal User user) {
        log.info("[{}]-> GET {}", user.getUsername(), API_PATH);
        Page<Account> results = rAccount.findAllByUsernameIsNot("admin", pageable);
        log.info("[{}]<- GET {}", user.getUsername(), API_PATH);
        return results;
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @Secured({"ROLE_ADMIN"})
    public Account update(@RequestBody Account value, @AuthenticationPrincipal User user) throws JPAException {
        log.info("[{}]-> PUT {}::{}", user.getUsername(), API_PATH, value.getId());
        //TODO:[generate TICKET]: null check
        Account item = rAccount.findById(value.getId()).get();
        if (item.getOid() == null) {
            item.setEmail(value.getEmail());
            if (value.getPassword() != null) {
                item.setPassword(passwordEncoder.encode(value.getPassword()));
            }
            item.setUsername(value.getUsername());
            item.setFirstName(value.getFirstName());
            item.setLastName(value.getLastName());
            item.setPatronymicName(value.getPatronymicName());
            item.setLocations(value.getLocations());
            item.setOrganizations(value.getOrganizations());
            item.setIsActive(value.getIsActive());
            item.setRoles(value.getRoles());
        } else {
            item.setLocations(value.getLocations());
            item.setOrganizations(value.getOrganizations());
            item.setIsActive(value.getIsActive());
            item.setRoles(value.getRoles());
        }
        try {
            item = rAccount.save(item);
        } catch (Exception e) {
            log.warn("[{}]<- PUT {}::{}", user.getUsername(), API_PATH, value.getId());
            throw new JPAException(e.getMessage());
        }
        log.info("[{}]<- PUT {}::{}", user.getUsername(), API_PATH, value.getId());
        return item;
    }

    @Deprecated
    @PutMapping(value = "/{id}/", consumes = "application/json", produces = "application/json")
    @Secured({"ROLE_ADMIN"})
    public void updatePWD(@PathVariable(name = "id") final User account, @RequestBody String value, @AuthenticationPrincipal User user) throws JPAException {
        log.info("[{}]-> PUT {}::{}", user.getUsername(), API_PATH, account.getId());
        account.setPassword(passwordEncoder.encode(value));
        rUser.save(account);
        log.info("[{}]<- PUT {}::{}", user.getUsername(), API_PATH, account.getId());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Secured({"ROLE_ADMIN"})
    public Account create(@RequestBody Account value, @AuthenticationPrincipal User user) throws JPAException {
        log.info("[{}]-> POST {}::{}", user.getUsername(), API_PATH, value);
        User item = new User();
        item.setUsername(value.getUsername());
        item.setEmail(value.getEmail());
        item.setPassword(passwordEncoder.encode(value.getPassword()));
        item.setFirstName(value.getFirstName());
        item.setLastName(value.getLastName());
        item.setPatronymicName(value.getPatronymicName());
        item.setIsActive(Boolean.TRUE);
        item.setCreateDateTime(LocalDateTime.now());
        List<UserRole> roles = new ArrayList();
        roles.addAll(value.getRoles());
        item.setRoles(roles);
        if(value.getLocations()!= null){
            item.setLocations(rLocation.findAllById(value.getLocations().stream().map(i->i.getId()).collect(Collectors.toList())));
        }
        if(value.getOrganizations()!= null){
            item.setOrganizations(rOrganization.findAllById(value.getOrganizations().stream().map(i->i.getId()).collect(Collectors.toList())));
        }
        try {
            item = rUser.save(item);
        } catch (Exception e) {
            log.warn("[{}]<- POST {}", user.getUsername(), API_PATH);
            throw new JPAException(e.getMessage());
        }
        Account result = rAccount.findById(item.getId()).get();
        log.info("[{}]<- POST {}::{}", user.getUsername(), API_PATH, result.getId());
        return result;
    }
}
