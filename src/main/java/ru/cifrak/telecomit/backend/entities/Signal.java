package ru.cifrak.telecomit.backend.entities;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Signal {
    ANLG(1, "Аналоговое"),
    DIGT(2, "Цифровое");

    private Integer id;
    private String name;

    Signal(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
