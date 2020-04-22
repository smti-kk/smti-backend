package ru.cifrak.telecomit.backend.api.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CapabilitiesCriteria {
    private int page = 0;

    @JsonProperty("page_size")
    private int pageSize = 25;
    
    private List<String> filter;
}
