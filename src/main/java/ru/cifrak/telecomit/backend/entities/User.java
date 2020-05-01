package ru.cifrak.telecomit.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

//@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Inheritance(strategy = InheritanceType.JOINED)

@Entity
@Table(name = "core_user", indexes = {
        @Index(columnList = "oid", name = "oid_idx"),
})
public class User implements Serializable {
    @Id
    @SequenceGenerator(name = "CORE_USER_ID_GENERATOR", sequenceName = "core_user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = " CORE_USER_ID_GENERATOR")
    protected Long id;

    @Column
    protected Long oid;

    @NotEmpty
    @Column(unique = true, nullable = false, columnDefinition = "text")
    protected String username;

    @NonNull
    @Column(nullable = false, columnDefinition = "text")
    protected String password;

    @NonNull
    @Column(name = "is_active", nullable = false)
    protected Boolean isActive = true;

    @NonNull
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "id"))
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected List<UserRole> roles = new ArrayList<>();

    @NonNull
    @Column(name = "first_name", nullable = false, columnDefinition = "text")
    protected String firstName;

    @NonNull
    @Column(name = "last_name", nullable = false, columnDefinition = "text")
    protected String lastName = "";

    @NonNull
    @Column(name = "patronymic_name", nullable = false, columnDefinition = "text")
    protected String patronymicName = "";

    @Column(columnDefinition = "text")
    protected String phone;

    @NonNull
    @Column(nullable = false, columnDefinition = "text")
    protected String passport = "";

    @NonNull
    @Column(nullable = false, columnDefinition = "text")
    protected String email = "";

    @NonNull
    @Column(nullable = false, columnDefinition = "text")
    protected String location = "";

    @NonNull
    @Column(name = "create_date_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime createDateTime;
}
