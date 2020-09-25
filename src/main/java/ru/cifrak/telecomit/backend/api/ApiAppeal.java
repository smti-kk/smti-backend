package ru.cifrak.telecomit.backend.api;

import io.swagger.annotations.ApiParam;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.entities.Appeal;
import ru.cifrak.telecomit.backend.entities.User;

import java.util.List;

@RequestMapping("/api/appeals")
public interface ApiAppeal {
    @GetMapping
    @Secured("ROLE_ADMIN")
    List<Appeal> findAll(@AuthenticationPrincipal User user);

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    Appeal getOne(@PathVariable Integer id,@AuthenticationPrincipal User user);

    @DeleteMapping("{id}")
    @Secured("ROLE_ADMIN")
    void delete(@PathVariable Integer id, @AuthenticationPrincipal User user);

    @PostMapping
    @Secured("ROLE_ADMIN")
    Appeal updateOrCreate(@RequestBody Appeal appeal, @AuthenticationPrincipal User user);
}
