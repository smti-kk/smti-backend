package ru.cifrak.telecomit.backend.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Validated
public interface ApiCompleatAddress {

    /**
     * GET /compleat :
     * Дополнение введенных адрессов пользователя
     *
     * @param address Вариант строки, что на данный момент ввел пользователь
     * @return OK (status code 200)
     */
    @GetMapping(
            value = "/compleat",
            produces = { "application/json" }
    )
    default List<AddressDto> getVariantsForCompleat(@RequestParam(value = "address", required = true) String address) {
        //Implement me :)
        return null;
    };
}
