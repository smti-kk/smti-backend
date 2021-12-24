package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class ExtZabbixHost {
    List<ExtZabbixTrigger> triggers;

    public ExtZabbixTrigger triggerUnavailable() {
        AtomicReference<ExtZabbixTrigger> result = new AtomicReference<>();
        triggers.forEach(trigger -> {
            if (trigger.getType() == TypeZabbixTrigger.UNAVAILABLE) {
                result.set(trigger);
            }
        });
        return result.get();
    }
}
