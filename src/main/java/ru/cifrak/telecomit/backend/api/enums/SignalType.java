package ru.cifrak.telecomit.backend.api.enums;

import lombok.Data;

@Data
public class SignalType {
    private Integer id;
    private String name;

    public SignalType(String id) {
        this.id = Integer.parseInt(id);

        switch (id) {
            case "1":
                this.name = "Аналоговый";
                break;
            case "2":
                this.name = "Цифровой";
                break;
        }
    }
}
