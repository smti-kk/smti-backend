package ru.cifrak.telecomit.backend.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.lang.NonNull;
import ru.cifrak.telecomit.backend.entities.User;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "AuthToken")
public class AuthTokenCache implements Serializable {
    @NonNull
    @Id
    private String id;

    @NonNull
    private LocalDateTime created;

    @NonNull
    @Indexed
    private Long userId;

    public AuthTokenCache(@NonNull String key,
                          @NonNull User user,
                          @NonNull ZoneId zoneId) {
        this.id = key;
        this.created = LocalDateTime.now(zoneId);
        this.userId = user.getId();
    }

    @TimeToLive
    public long getTimeToLive() {
        return 1 * 60 * 60;
    }
}
