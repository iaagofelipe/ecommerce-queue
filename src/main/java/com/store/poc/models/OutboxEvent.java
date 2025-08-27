package com.store.poc.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "outbox", indexes = {
        @Index(name = "ix_outbox_published_created", columnList = "published, createdAt")
})
public class OutboxEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID aggregateId;                 // orderId

    @Column(nullable = false, length = 50, updatable = false)
    private String eventType;                 // ex: ORDER_PLACED

    @Lob @Column(nullable = false, updatable = false)
    private String payload;                   // JSON a enviar

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void pre() {
        createdAt = Instant.now();
    }
}