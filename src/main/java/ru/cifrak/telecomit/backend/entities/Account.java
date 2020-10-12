package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data

@Entity
@Table(name = "app_user")

@NamedEntityGraph(
        name = Account.WITH_ALL,
        attributeNodes = {
                @NamedAttributeNode("roles"),
                @NamedAttributeNode("organizations"),
                @NamedAttributeNode(value = "locations", subgraph = Account.WITH_SG)
        },
        subgraphs = {
                @NamedSubgraph(name = Account.WITH_SG, attributeNodes = {@NamedAttributeNode("parent")})
        }
)
public class Account {
    public static final String WITH_ALL = "account_roles";
    public static final String WITH_SG = "account_location_parent";

    @Id
    private Long id;

    @Column
    private String username;

    @Column(name = "is_active")
    private Boolean isActive;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "id"))
    @Column
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "patronymic_name")
    private String patronymicName;

    @Column
    private String email;


    private String password;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @Column
    private Long oid;

    @OneToMany
    @JoinTable(name = "user_locations",
            joinColumns = {@JoinColumn(name = "key_user")},
            inverseJoinColumns = {@JoinColumn(name = "key_location")})
    @JsonIgnoreProperties({"parent"})
    private Set<DLocationBase> locations;

    @OneToMany
    @JoinTable(name = "user_organizations",
            joinColumns = {@JoinColumn(name = "key_user")},
            inverseJoinColumns = {@JoinColumn(name = "key_organization")})
    @JsonIgnoreProperties({"location","parent","children","accessPoints","type","smo"})
    private Set<Organization> organizations;

}
