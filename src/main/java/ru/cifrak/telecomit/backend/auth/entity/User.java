package ru.cifrak.telecomit.backend.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.lang.NonNull;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
//@Audited
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
//@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "core_user", indexes = {
        @Index(columnList = "oid", name = "oid_idx"),
})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "create_date_time", nullable = false)
    private LocalDateTime createDateTime;
}
