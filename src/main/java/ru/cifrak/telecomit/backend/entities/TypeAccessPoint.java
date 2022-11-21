package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeAccessPoint {
    ESPD(1,"ESPD","ЕСПД"),
//    RSMO(2,"RSMO","РСЗО"),
    SMO(3,"SMO","СЗО"),
    ZSPD(4,"ZSPD","ЗСПД"),
    CONTRACT(5,"CONTRACT","Контракт");

    private Integer id;
    private String name;
    private String desc;

    @Override
    public String toString() {
        switch (this) {
            case ESPD:      return "ЕСПД";
//            case RSMO:      return "РСЗО";
            case SMO:       return "СЗО";
            case ZSPD:      return "ЗСПД";
            case CONTRACT:  return "Контракт";
            default:    throw new IllegalArgumentException();
        }
    }

}
