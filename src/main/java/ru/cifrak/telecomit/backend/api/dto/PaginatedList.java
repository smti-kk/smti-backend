package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedList<T> {
    Long count;
    String next;
    String previous;
    List<T> results;

    public PaginatedList(Long count, List<T> results) {
        this.count = count;
        this.results = results;
    }
}
