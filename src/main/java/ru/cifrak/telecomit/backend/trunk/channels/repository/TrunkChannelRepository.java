package ru.cifrak.telecomit.backend.trunk.channels.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;
import ru.cifrak.telecomit.backend.trunk.channels.entity.TrunkChannel;

import java.util.List;
import java.util.Optional;


public interface TrunkChannelRepository extends JpaRepository<TrunkChannel, Integer> {
    @Override
    @EntityGraph("trunk_channel_full")
    @NotNull
    List<TrunkChannel> findAll();

    @EntityGraph("trunk_channel_full")
    List<TrunkChannel> findByLocationStartAndLocationEndAndOperator(
            MapLocation locationStart,
            MapLocation locationEnd,
            Operator operator
    );

    @Override
    @EntityGraph("trunk_channel_full")
    Optional<TrunkChannel> findById(Integer integer);
}
