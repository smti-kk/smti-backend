package ru.cifrak.telecomit.backend.trunk.channels.controller;

import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.trunk.channels.entity.TrunkChannel;

import java.util.List;

@RequestMapping("/api/trunk-channels")
public interface TrunkChannelController {
    @GetMapping
    List<TrunkChannel> list();

    @GetMapping("/{trunkChannel}")
    TrunkChannel one(@PathVariable TrunkChannel trunkChannel) throws NotFoundException;

    @DeleteMapping("/{trunkChannelId}")
    void remove(@PathVariable Integer trunkChannelId);

    @PostMapping
    TrunkChannel save(@RequestBody TrunkChannel trunkChannel);

    @PutMapping
    TrunkChannel update(@RequestBody TrunkChannel trunkChannel);
}
