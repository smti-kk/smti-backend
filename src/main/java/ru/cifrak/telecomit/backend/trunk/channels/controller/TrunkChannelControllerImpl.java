package ru.cifrak.telecomit.backend.trunk.channels.controller;

import org.springframework.security.access.annotation.Secured;
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

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    public List<TrunkChannel> list() {
        return trunkChannelRepository.findAll();
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public TrunkChannel one(Integer trunkChannelId) throws NotFoundException {
        return trunkChannelRepository
                .findById(trunkChannelId)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public void remove(Integer trunkChannelId) {
        trunkChannelRepository.deleteById(trunkChannelId);
    }

    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public TrunkChannel save(TrunkChannel trunkChannel) {
        return trunkChannelRepository.save(trunkChannel);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public TrunkChannel update(TrunkChannel trunkChannel) {
        if (trunkChannel.getProgram() == null){
            trunkChannel.setCompleted(null);
        }
        return trunkChannelRepository.save(trunkChannel);
    }
}
