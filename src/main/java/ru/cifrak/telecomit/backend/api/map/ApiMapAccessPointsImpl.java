package ru.cifrak.telecomit.backend.api.map;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.repository.map.MapAccessPointRepository;
import ru.cifrak.telecomit.backend.repository.dto.MapAccessPoint;
import ru.cifrak.telecomit.backend.service.BboxFactory;

import java.util.List;

@RestController
public class ApiMapAccessPointsImpl implements ApiMapAccessPoints {
    private final MapAccessPointRepository mapAccessPointRepository;
    private final BboxFactory bboxFactory;

    public ApiMapAccessPointsImpl(MapAccessPointRepository mapAccessPointRepository, BboxFactory bboxFactory) {
        this.mapAccessPointRepository = mapAccessPointRepository;
        this.bboxFactory = bboxFactory;
    }

    @Override
    public List<MapAccessPoint> list(String type) {
        return mapAccessPointRepository.findAll(type);
    }

    @Override
    public List<MapAccessPoint> list(String type, List<Double> bbox) {
        return mapAccessPointRepository.findAllByBbox(
                bboxFactory.createPolygon(bbox),
                type
        );
    }
}
