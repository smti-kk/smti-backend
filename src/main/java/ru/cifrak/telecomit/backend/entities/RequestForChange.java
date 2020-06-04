package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table
public class RequestForChange extends Auditing implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "REQUEST_FOR_CHANGE_ID_GENERATOR", sequenceName = "request_for_change_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_FOR_CHANGE_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "key_user", nullable = false)
    private User owner;

    @Column(nullable = false)
    private LocalDateTime started;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_location", nullable = false)
    private Location location;

    @Column(columnDefinition = "text")
    private String comment;

}
