package ru.cifrak.telecomit.backend.api.dto.external.utm5;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtUtmDtoAuth {
    String username;
    String password;
}
