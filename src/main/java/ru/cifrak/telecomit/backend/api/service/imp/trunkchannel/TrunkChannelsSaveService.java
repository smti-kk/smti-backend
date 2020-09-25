package ru.cifrak.telecomit.backend.api.service.imp.trunkchannel;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.repository.RepositoryGovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryTypeTruncChannel;
import ru.cifrak.telecomit.backend.repository.map.MapLocationsPositionRepository;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.trunk.channels.entity.TrunkChannel;
import ru.cifrak.telecomit.backend.trunk.channels.repository.TrunkChannelRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TrunkChannelsSaveService {

    private final RepositoryOperator repositoryOperator;

    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;

    private final MapLocationsPositionRepository mapLocationsPositionRepository;

    private final TrunkChannelRepository trunkChannelRepository;

    private final LocationService locationService;

    private RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram;

    public TrunkChannelsSaveService(
            RepositoryOperator repositoryOperator,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel,
            MapLocationsPositionRepository mapLocationsPositionRepository,
            TrunkChannelRepository trunkChannelRepository,
            LocationService locationService,
            RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram) {
        this.repositoryOperator = repositoryOperator;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
        this.mapLocationsPositionRepository = mapLocationsPositionRepository;
        this.trunkChannelRepository = trunkChannelRepository;
        this.locationService = locationService;
        this.repositoryGovernmentDevelopmentProgram = repositoryGovernmentDevelopmentProgram;
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
                if (!tcDTO.getComissioning().isEmpty()) {
                    tces.get(0).setCommissioning(LocalDate.of(
                            Integer.parseInt(tcDTO.getComissioning().substring(6, 10)),
                            Integer.parseInt(tcDTO.getComissioning().substring(3, 5)),
                            Integer.parseInt(tcDTO.getComissioning().substring(0, 2))
                    ));
                }
                tces.get(0).setProgram(repositoryGovernmentDevelopmentProgram.findByAcronym(tcDTO.getProgram()));
                trunkChannelRepository.save(tces.get(0));
            } else {
                TrunkChannel tc = new TrunkChannel();
                tc.setLocationStart(
                        mapLocationsPositionRepository.findByFias(UUID.fromString(tcDTO.getLocationStart())));
                tc.setLocationEnd(
                        mapLocationsPositionRepository.findByFias(UUID.fromString(tcDTO.getLocationEnd())));
                tc.setOperator(repositoryOperator.findByName(tcDTO.getOperator()));
                tc.setTypeTrunkChannel(repositoryTypeTruncChannel.findByName(tcDTO.getType()));
                if (!tcDTO.getComissioning().isEmpty()) {
                    tc.setCommissioning(LocalDate.of(
                            Integer.parseInt(tcDTO.getComissioning().substring(6, 10)),
                            Integer.parseInt(tcDTO.getComissioning().substring(3, 5)),
                            Integer.parseInt(tcDTO.getComissioning().substring(0, 2))
                    ));
                }
                tc.setProgram(repositoryGovernmentDevelopmentProgram.findByAcronym(tcDTO.getProgram()));
                // TODO: Transaction.
                trunkChannelRepository.save(tc);
            }
            locationService.refreshCache();
        }
    }
}
