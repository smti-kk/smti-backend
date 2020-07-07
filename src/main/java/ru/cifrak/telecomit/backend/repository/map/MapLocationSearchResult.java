package ru.cifrak.telecomit.backend.repository.map;

import lombok.Getter;

@Getter
public class MapLocationSearchResult {
    private final Integer id;
    private final String name;
    private final String type;
    private final String parentName;
    private final String parentType;

    public MapLocationSearchResult(Integer id, String name, String type, String parentName, String parentType) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parentName = parentName;
        this.parentType = parentType;
    }
}
