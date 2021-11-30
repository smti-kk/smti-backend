package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.cifrak.telecomit.backend.entities.ImportanceProblemStatus;

@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class ExtZabbixTrigger {
    String triggerId;
    String description;
    ImportanceProblemStatus importance;
    boolean value;
}
