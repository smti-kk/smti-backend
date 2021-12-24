package ru.cifrak.telecomit.backend.api;

public class AddressDto {
    private final Integer id;
    private final String address;


    public AddressDto(Integer id, String address) {
        this.id = id;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }
}
