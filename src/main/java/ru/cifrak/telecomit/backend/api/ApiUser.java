package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryAccount;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryUser;
import ru.cifrak.telecomit.backend.entities.Account;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/")
public class ApiUser {
    private final RepositoryUser rUser;
    private final RepositoryAccount rAccount;

    public ApiUser(RepositoryUser userRepository, RepositoryAccount rAccount) {
        this.rUser = userRepository;
        this.rAccount = rAccount;
    }

    @GetMapping
    public List<Account> list() {
        return rAccount.findAll();
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> update(@RequestBody Account value) {
        log.info("->PUT /api/user/::{}", value.getId());
        //TODO:[generate TICKET]: null check
        Account item = rAccount.findById(value.getId()).get();
        item.setEmail(value.getEmail());
        item.setFirstName(value.getFirstName());
        item.setLastName(value.getLastName());
        item.setPatronymicName(value.getPatronymicName());
        item.setLocations(value.getLocations());
        item.setIsActive(value.getIsActive());
        item.setRoles(value.getRoles());
        item = rAccount.save(item);
        log.info("<-PUT /api/user/::{}", value.getId());
        return ResponseEntity.ok(item);
    }
}
