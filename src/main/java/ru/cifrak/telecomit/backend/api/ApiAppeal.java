package ru.cifrak.telecomit.backend.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.entities.*;

import java.time.LocalDate;

@RequestMapping("/api/appeals")
public interface ApiAppeal {
    @GetMapping
    @Secured("ROLE_ADMIN")
    Page<Appeal> findAll(@AuthenticationPrincipal User user,
                         Pageable pageable);

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    Appeal getOne(@PathVariable Integer id, @AuthenticationPrincipal User user);

    @DeleteMapping("{id}")
    @Secured("ROLE_ADMIN")
    void delete(@PathVariable Integer id, @AuthenticationPrincipal User user);

    @PostMapping
    @Secured("ROLE_ADMIN")
    Appeal updateOrCreate(@RequestParam(required = false) Integer id,
                          @RequestParam String title,
                          @RequestParam AppealStatus status,
                          @RequestParam AppealPriority priority,
                          @RequestParam AppealLevel level,
                          @RequestParam(required = false) Integer locationId,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
                          @RequestParam(required = false) MultipartFile file,
                          @RequestParam(required = false) MultipartFile responseFile,
                          @AuthenticationPrincipal User user);
}
