package ru.cifrak.telecomit.backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class ApiEsiaExchanheToken {

    @GetMapping("/login/")
    public void login() {}

    @PostMapping("/get_token/")
    public ResponseEntity<Map<String, Object>> exchangeTempToken(@RequestBody Map<String, String> data) {
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("token", "123");
        }});
    }

    @GetMapping("/account_info")
    public ResponseEntity<User> account_info() {
        return ResponseEntity.ok(new User("username123"));
    }
}