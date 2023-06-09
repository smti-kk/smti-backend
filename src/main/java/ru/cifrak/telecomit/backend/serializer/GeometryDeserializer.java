package ru.cifrak.telecomit.backend.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.*;

import java.io.IOException;

public class GeometryDeserializer extends JsonDeserializer<Geometry> {

    private GeometryFactory gf = new GeometryFactory();

    @Override
    public Geometry deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();
        JsonNode root = oc.readTree(jp);
        return parseGeometry(root);
    }

    private Geometry parseGeometry(JsonNode root) {
        JsonNode type = root.get("type");
        String typeName;
        if (type == null) {
            typeName = "SimplePoint";
        } else {
            typeName = type.asText();
        }
        switch (typeName) {
            case "SimplePoint":
                return gf.createPoint(parseCoordinateAsLatLng(root));

            case "Point":
                return gf.createPoint(parseCoordinate(root
                        .get("coordinates")));

            case "MultiPoint":
                return gf.createMultiPoint(parseLineString(root.get("coordinates")));

            case "LineString":
                return gf.createLineString(parseLineString(root.get("coordinates")));

            case "MultiLineString":
                return gf.createMultiLineString(parseLineStrings(root
                        .get("coordinates")));

            case "Polygon":
                JsonNode arrayOfRings = root.get("coordinates");
                return parsePolygonCoordinates(arrayOfRings);

            case "MultiPolygon":
                JsonNode arrayOfPolygons = root.get("coordinates");
                return gf.createMultiPolygon(parsePolygons(arrayOfPolygons));

            case "GeometryCollection":
                return gf.createGeometryCollection(parseGeometries(root
                        .get("geometries")));
            default:
                throw new UnsupportedOperationException();
        }
    }

    private Geometry[] parseGeometries(JsonNode arrayOfGeoms) {
        Geometry[] items = new Geometry[arrayOfGeoms.size()];
        for (int i = 0; i != arrayOfGeoms.size(); ++i) {
            items[i] = parseGeometry(arrayOfGeoms.get(i));
        }
        return items;
    }

    private Polygon parsePolygonCoordinates(JsonNode arrayOfRings) {
        return gf.createPolygon(parseExteriorRing(arrayOfRings),
                parseInteriorRings(arrayOfRings));
    }

    private Polygon[] parsePolygons(JsonNode arrayOfPolygons) {
        Polygon[] polygons = new Polygon[arrayOfPolygons.size()];
        for (int i = 0; i != arrayOfPolygons.size(); ++i) {
            polygons[i] = parsePolygonCoordinates(arrayOfPolygons.get(i));
        }
        return polygons;
    }

    private LinearRing parseExteriorRing(JsonNode arrayOfRings) {
        return gf.createLinearRing(parseLineString(arrayOfRings.get(0)));
    }

    private LinearRing[] parseInteriorRings(JsonNode arrayOfRings) {
        LinearRing rings[] = new LinearRing[arrayOfRings.size() - 1];
        for (int i = 1; i < arrayOfRings.size(); ++i) {
            rings[i - 1] = gf.createLinearRing(parseLineString(arrayOfRings
                    .get(i)));
        }
        return rings;
    }

    private Coordinate parseCoordinate(JsonNode array) {
        return new Coordinate(array.get(0).asDouble(), array.get(1).asDouble());
    }

    private Coordinate parseCoordinateAsLatLng(JsonNode obj) {
        return new Coordinate(obj.get("lng").asDouble(), obj.get("lat").asDouble());
    }

    private Coordinate[] parseLineString(JsonNode array) {
        Coordinate[] points = new Coordinate[array.size()];
        for (int i = 0; i != array.size(); ++i) {
            points[i] = parseCoordinateAsLatLng(array.get(i));
        }
        return points;
    }

    private LineString[] parseLineStrings(JsonNode array) {
        LineString[] strings = new LineString[array.size()];
        for (int i = 0; i != array.size(); ++i) {
            strings[i] = gf.createLineString(parseLineString(array.get(i)));
        }
        return strings;
    }

}