package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@RequestMapping("/api/autocomplit")
public interface ApiCompleatAddress {

    /**
     * Поиск вхождений введённых адрессов пользователя.
     *
     * @param address строка, которую на данный момент ввёл пользователь
     * @return возвращает не более 10 вариантов вхождений
     */
    @GetMapping
    List<String> getVariantsForCompleat(@RequestParam(value = "address") String address);
}
