package com.store.poc.models.dto;

import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String sku,
        String name,
        String description,
        Long priceCents,
        String imageUrl,
        Integer stockQty,
        Boolean active,
        Instant createdAt,
        Instant updatedAt
) {}
