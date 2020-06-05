package ru.cifrak.telecomit.backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.service.ServiceTechnicalCapabilities;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tc")
public class ApiTecnicalCapabilities {
    private final ServiceTechnicalCapabilities servise;

    public ApiTecnicalCapabilities(ServiceTechnicalCapabilities servise) {
        this.servise = servise;
    }

    @GetMapping("/ats/")
    public ResponseEntity<List<TcAts>> ats(){
        return ResponseEntity.ok(servise.allAts());
    }
    @GetMapping("/internet/")
    public ResponseEntity<List<TcInternet>> internet(){
        return ResponseEntity.ok(servise.allInternet());
    }
    @GetMapping("/mobile/")
    public ResponseEntity<List<TcMobile>> mobile(){
        return ResponseEntity.ok(servise.allMobile());
    }
    @GetMapping("/post/")
    public ResponseEntity<List<TcPost>> post(){
        return ResponseEntity.ok(servise.allPost());
    }
    @GetMapping("/radio/")
    public ResponseEntity<List<TcRadio>> radio(){
        return ResponseEntity.ok(servise.allRadio());
    }
    @GetMapping("/tv/")
    public ResponseEntity<List<TcTv>> tv(){
        return ResponseEntity.ok(servise.allTv());
    }

    @GetMapping("/w/post/")
    public ResponseEntity wPost(){
        TcPost post = new TcPost();
        post.setType(TypePost.POST);
        post.setLocation(servise.getLoc());
        post.setOperator(servise.getOpe());
        post.setQuality(ServiceQuality.ABSENT);
        return ResponseEntity.ok(servise.save(post));
    }
}
