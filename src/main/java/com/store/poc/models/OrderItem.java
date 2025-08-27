package com.store.poc.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private PurchaseOrder order;

    @Column(nullable = false, length = 64)
    private String sku;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Long priceCents;

}
