package ru.cifrak.telecomit.backend.serializer;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

public class GeometrySerializer extends JsonSerializer<Geometry> {

    @Override
    public void serialize(Geometry value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException,
            JsonProcessingException {

        writeGeometry(jgen, value);
    }

    public void writeGeometry(JsonGenerator jgen, Geometry value)
            throws JsonGenerationException, IOException {
        if (value instanceof Polygon) {
            writePolygon(jgen, (Polygon) value);

        } else if(value instanceof Point) {
            writePoint(jgen, (Point) value);

        } else if (value instanceof MultiPoint) {
            writeMultiPoint(jgen, (MultiPoint) value);

        } else if (value instanceof MultiPolygon) {
            writeMultiPolygon(jgen, (MultiPolygon) value);

        } else if (value instanceof LineString) {
            writeLineString(jgen, (LineString) value);

        } else if (value instanceof MultiLineString) {
            writeMultiLineString(jgen, (MultiLineString) value);
        } else if (value instanceof GeometryCollection) {
            writeGeometryCollection(jgen, (GeometryCollection) value);

        } else {
            throw new UnsupportedOperationException("not implemented: "
                    + value.getClass().getName());
        }
    }

    private void writeGeometryCollection(JsonGenerator jgen,
                                         GeometryCollection value) throws JsonGenerationException,
            IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "GeometryCollection");
        jgen.writeArrayFieldStart("geometries");

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeGeometry(jgen, value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writeMultiPoint(JsonGenerator jgen, MultiPoint value)
            throws JsonGenerationException, IOException {

        jgen.writeStartObject();
        jgen.writeStringField("type", "MultiPoint");
        jgen.writeArrayFieldStart("coordinates");

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePointCoordsAsNumberArray(jgen, (Point) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writeMultiLineString(JsonGenerator jgen, MultiLineString value)
            throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "MultiLineString");
        jgen.writeArrayFieldStart("coordinates");

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeLineStringCoords(jgen, (LineString) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    @Override
    public Class<Geometry> handledType() {
        return Geometry.class;
    }

    private void writeMultiPolygon(JsonGenerator jgen, MultiPolygon value)
            throws JsonGenerationException, IOException {

        //*
        jgen.writeStartObject();
        jgen.writeStringField("type", "MultiPolygon");
        jgen.writeArrayFieldStart("coordinates");

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePolygonCoordinates(jgen, (Polygon) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();

        /*/
        // TODO: This is workaround for FRONTEND WHEN IT waits for string instead of json-Object
        StringWriter buff = new StringWriter();
        JsonGenerator new_jgen = new JsonFactory().createGenerator(buff);

        new_jgen.writeStartObject();
        new_jgen.writeStringField("type", "MultiPolygon");
        new_jgen.writeArrayFieldStart("coordinates");

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePolygonCoordinates(new_jgen, (Polygon) value.getGeometryN(i));
        }

        new_jgen.writeEndArray();
        new_jgen.writeEndObject();

        new_jgen.close();

        jgen.writeString(buff.toString());
        /**/
    }

    private void writePolygon(JsonGenerator jgen, Polygon value)
            throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "Polygon");
        jgen.writeFieldName("coordinates");
        writePolygonCoordinates(jgen, value);

        jgen.writeEndObject();
    }

    private void writePolygonCoordinates(JsonGenerator jgen, Polygon value)
            throws IOException, JsonGenerationException {
        jgen.writeStartArray();
        writeLineStringCoords(jgen, value.getExteriorRing());

        for (int i = 0; i != value.getNumInteriorRing(); ++i) {
            writeLineStringCoords(jgen, value.getInteriorRingN(i));
        }
        jgen.writeEndArray();
    }

    private void writeLineStringCoords(JsonGenerator jgen, LineString ring)
            throws JsonGenerationException, IOException {
        jgen.writeStartArray();
        for (int i = 0; i != ring.getNumPoints(); ++i) {
            Point p = ring.getPointN(i);
            writePointCoordsAsNumberArray(jgen, p);
        }
        jgen.writeEndArray();
    }

    private void writeLineString(JsonGenerator jgen, LineString lineString)
            throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "LineString");
        jgen.writeFieldName("coordinates");
        writeLineStringCoords(jgen, lineString);
        jgen.writeEndObject();
    }

    private void writePoint(JsonGenerator jgen, Point p)
            throws JsonGenerationException, IOException {
        writePointCoords(jgen, p);
    }

    private void writePointCoords(JsonGenerator jgen, Point p)
            throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeNumberField("lng", p.getX());
        jgen.writeNumberField("lat", p.getY());
        jgen.writeEndObject();
    }

    private void writePointCoordsAsNumberArray(JsonGenerator jgen, Point p)
            throws IOException, JsonGenerationException {
        jgen.writeStartArray();
        jgen.writeNumber(p.getX());
        jgen.writeNumber(p.getY());
        jgen.writeEndArray();
    }
}