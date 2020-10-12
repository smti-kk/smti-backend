package ru.cifrak.telecomit.backend.api.dto.external.utm5;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtUtmDtoResponseUser {
    private String user_id;
    private String login;
    private String password;
    private String basic_account;
    private String full_name;
    private String email;
    private String contract_id;
    private String advance_payment;
    private String card_user;
    private List<Integer> slinks;
    private List<Integer> groups;
    private List<String> accounts;
    private String till;
}
