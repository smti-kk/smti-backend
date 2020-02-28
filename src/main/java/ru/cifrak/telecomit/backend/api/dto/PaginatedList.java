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
}
