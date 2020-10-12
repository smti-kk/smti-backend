package ru.cifrak.telecomit.backend.api.dto.response;

import lombok.Value;

import java.util.List;

@Value
public class ExternalSystemCreateStatusDTO {
    String message;
    List<String> errors;

    public ExternalSystemCreateStatusDTO(String s, List<String> errors) {
        message = s;
        this.errors = errors;
    }

    public ExternalSystemCreateStatusDTO(String s) {
        this(s, null);
    }
}
