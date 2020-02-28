package ru.cifrak.telecomit.backend.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/esia-auth")
public class ApiEsia {

    @GetMapping("/oauth-link/")
    public ResponseEntity getAuthLink() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/login?temp_token=123"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
