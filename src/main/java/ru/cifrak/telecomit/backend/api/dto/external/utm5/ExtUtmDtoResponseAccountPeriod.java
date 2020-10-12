package ru.cifrak.telecomit.backend.api.dto.external.utm5;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtUtmDtoResponseAccountPeriod {
    private Long begin;
    private Long canonical_len;
    private Long charge_interval;
    private Long custom_duration;
    private Long end;
    private Long id;
    private Long invoice_month;
    private Long next_accounting_period_id;
    private Long periodic_type;
    private Long static_id;
}
