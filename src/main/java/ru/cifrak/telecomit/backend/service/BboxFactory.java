package ru.cifrak.telecomit.backend.service;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BboxFactory extends GeometryFactory {
    public Polygon createPolygon(List<Double> bbox) {
        Coordinate southWest = new Coordinate(bbox.get(0), bbox.get(1));
        Coordinate northEast = new Coordinate(bbox.get(2), bbox.get(3));
        Coordinate southEast = new Coordinate(bbox.get(0), bbox.get(3));
        Coordinate northWest = new Coordinate(bbox.get(2), bbox.get(1));

        Coordinate[] coordinates = new Coordinate[] { southWest, northWest, northEast, southEast, southWest };

        Polygon polygon = this.createPolygon(coordinates);
        polygon.setSRID(4326);
        return polygon;
    }
}
