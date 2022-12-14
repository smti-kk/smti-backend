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
    // ZSPD : ЗСПД - заменили на - EMSPD : ЕМСПД
    EMSPD(4,"EMSPD","ЕМСПД"),
    CONTRACT(5,"CONTRACT","Контракт"),
    GENERAL(6, "GENERAL", "Общее");

    private Integer id;
    private String name;
    private String desc;

    @Override
    public String toString() {
        switch (this) {
            case ESPD:      return "ЕСПД";
//            case RSMO:      return "РСЗО";
            case SMO:       return "СЗО";
            case EMSPD:      return "ЕМСПД";
            case CONTRACT:  return "Контракт";
            default:    throw new IllegalArgumentException();
        }
    }

}
