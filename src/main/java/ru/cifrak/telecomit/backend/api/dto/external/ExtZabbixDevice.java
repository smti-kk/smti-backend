package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class ExtZabbixDevice {
    String hostId;
    List<ExtZabbixTrigger> triggers;
}
