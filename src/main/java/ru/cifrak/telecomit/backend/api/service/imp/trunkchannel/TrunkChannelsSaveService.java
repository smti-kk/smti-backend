package ru.cifrak.telecomit.backend.api.service.imp.trunkchannel;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryTypeTruncChannel;
import ru.cifrak.telecomit.backend.repository.map.MapLocationsPositionRepository;
import ru.cifrak.telecomit.backend.trunk.channels.entity.TrunkChannel;
import ru.cifrak.telecomit.backend.trunk.channels.repository.TrunkChannelRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TrunkChannelsSaveService {

    private final RepositoryOperator repositoryOperator;

    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;

    private final MapLocationsPositionRepository mapLocationsPositionRepository;

    private final TrunkChannelRepository trunkChannelRepository;

    public TrunkChannelsSaveService(
            RepositoryOperator repositoryOperator,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel,
            MapLocationsPositionRepository mapLocationsPositionRepository,
            TrunkChannelRepository trunkChannelRepository) {
        this.repositoryOperator = repositoryOperator;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
        this.mapLocationsPositionRepository = mapLocationsPositionRepository;
        this.trunkChannelRepository = trunkChannelRepository;
    }

    public void save(List<TrunkChannelFromExcelDTO> TcesDTO) {
        for (TrunkChannelFromExcelDTO tcDTO : TcesDTO){
            List<TrunkChannel> tces = trunkChannelRepository.findByLocationStartAndLocationEndAndOperator(
                    mapLocationsPositionRepository.findByFias(UUID.fromString(tcDTO.getLocationStart())),
                    mapLocationsPositionRepository.findByFias(UUID.fromString(tcDTO.getLocationEnd())),
                    repositoryOperator.findByName(tcDTO.getOperator())
            );
            if (tces.size() > 0) {
                // TODO: Transaction.
                tces.get(0).setTypeTrunkChannel(repositoryTypeTruncChannel.findByName(tcDTO.getType()));
                trunkChannelRepository.save(tces.get(0));
            } else {
                TrunkChannel tc = new TrunkChannel();
                tc.setLocationStart(
                        mapLocationsPositionRepository.findByFias(UUID.fromString(tcDTO.getLocationStart())));
                tc.setLocationEnd(
                        mapLocationsPositionRepository.findByFias(UUID.fromString(tcDTO.getLocationEnd())));
                tc.setOperator(repositoryOperator.findByName(tcDTO.getOperator()));
                tc.setTypeTrunkChannel(repositoryTypeTruncChannel.findByName(tcDTO.getType()));
                // TODO: Transaction.
                trunkChannelRepository.save(tc);
            }
        }
    }
}
