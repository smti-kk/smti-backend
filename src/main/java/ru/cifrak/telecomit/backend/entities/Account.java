package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data

@Entity
@Table(name = "app_user")
@Immutable

@NamedEntityGraph(
        name = "account_roles",
        attributeNodes = {
                @NamedAttributeNode("roles")
        }
)
public class Account {
    @Id
    private Long id;

    @Column
    private String username;

    @JsonIgnore
    @Column
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "id"))
    @Column
    @Enumerated(EnumType.STRING)
//    @Fetch(FetchMode.JOIN)
    private List<UserRole> roles = new ArrayList<>();

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "patronymic_name")
    private String patronymicName;

    @Column
    private String phone;

    @Column
    private String passport;

    @Column
    private String email;

    @Column
    private String location;

    @Column(name = "create_date_time")
    private LocalDateTime createDateTime;
}
