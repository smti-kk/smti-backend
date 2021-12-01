package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class ExtZabbixDevice {
    List<ExtZabbixTrigger> triggers;

    public boolean triggerUnavailableExists() {
        AtomicBoolean result = new AtomicBoolean(false);
        triggers.forEach(trigger -> {
            if (trigger.getType() == TypeZabbixTrigger.UNAVAILABLE) {
                result.set(true);
            }
        });
        return result.get();
    }
}
