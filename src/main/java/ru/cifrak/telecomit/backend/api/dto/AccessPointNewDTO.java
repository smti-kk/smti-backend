package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.entities.TypeAccessPoint;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonIgnoreProperties(ignoreUnknown=true)
public class AccessPointNewDTO {
    private Integer id;
    private String address;
    private Integer billing_id;
    private Integer internetAccess;
    private String contractor;
    private String customer;
    private String description;
    private String declaredSpeed;
    private Integer government_program;
    private String ip_config;
    private Integer max_amount;
    private String name;
    private String node;
    private Integer operator;
    private String quality;
    private String state;
    private Integer ucn;
    private Boolean visible;
    private String type;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point point;
    private Integer completed;
    // ZSPD
    //
    private String hardware;
    private String software;
    // CONTRACT
    private String number;
    private Long amount;
    private LocalDate started;
    private LocalDate ended;
    private String equipment;
    private String softType;
}