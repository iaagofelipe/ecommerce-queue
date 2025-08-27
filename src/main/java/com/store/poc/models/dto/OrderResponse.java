package com.store.poc.models.dto;

import com.store.poc.models.enums.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID customerId,
        OrderStatus status,
        long totalCents,
        Instant createdAt,
        List<Item> items
) {
    public record Item(String sku, int qty, long priceCents) {}
}
