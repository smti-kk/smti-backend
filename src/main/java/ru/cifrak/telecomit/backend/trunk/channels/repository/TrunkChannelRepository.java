package ru.cifrak.telecomit.backend.trunk.channels.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.trunk.channels.entity.TrunkChannel;

import java.util.List;


public interface TrunkChannelRepository extends JpaRepository<TrunkChannel, Integer> {
    @Override
    @EntityGraph("trunk_channel_full")
    @NotNull
    List<TrunkChannel> findAll();
}
