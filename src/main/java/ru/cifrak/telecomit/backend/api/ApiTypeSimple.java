package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.Signal;
import ru.cifrak.telecomit.backend.entities.TypeAccessPoint;
import ru.cifrak.telecomit.backend.entities.TypePost;
@Slf4j
@RestController
@RequestMapping("/api/type/")
public class ApiTypeSimple {
    @GetMapping("/post/")
    public TypePost[] post() {
        return TypePost.values();
    }

    @GetMapping("/tv/")
    public Signal[] tv() {
        return Signal.values();
    }

    @GetMapping("/access-point/")
    public TypeAccessPoint[] ap() {
        return TypeAccessPoint.values();
    }

}
