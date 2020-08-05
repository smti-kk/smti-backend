package ru.cifrak.telecomit.backend.trunk.channels.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.trunk.channels.entity.TrunkChannel;
import ru.cifrak.telecomit.backend.trunk.channels.repository.TrunkChannelRepository;

import java.util.List;

@RestController
public class TrunkChannelControllerImpl implements TrunkChannelController {

    private final TrunkChannelRepository trunkChannelRepository;

    public TrunkChannelControllerImpl(TrunkChannelRepository trunkChannelRepository) {
        this.trunkChannelRepository = trunkChannelRepository;
    }

    public List<TrunkChannel> list() {
        return trunkChannelRepository.findAll();
    }

    public TrunkChannel one(TrunkChannel trunkChannel) throws NotFoundException {
        if (trunkChannel == null) {
            throw new NotFoundException();
        }
        return trunkChannel;
    }

    public void remove(Integer trunkChannelId) {
        trunkChannelRepository.deleteById(trunkChannelId);
    }

    public TrunkChannel save(TrunkChannel trunkChannel) {
        return trunkChannelRepository.save(trunkChannel);
    }

    @Override
    public TrunkChannel update(TrunkChannel trunkChannel) {
        return trunkChannelRepository.save(trunkChannel);
    }
}
