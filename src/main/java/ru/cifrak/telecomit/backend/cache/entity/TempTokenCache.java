package ru.cifrak.telecomit.backend.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RedisHash(value = "TempTokenCache", timeToLive = 60 * 10)
public class TempTokenCache implements Serializable {
    private String id;
    private String token;
}
