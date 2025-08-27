package com.store.poc.models.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
        @Size(max = 200) String name,
        @Size(max = 2000) String description,
        @Positive Long priceCents,
        @Size(max = 512) String imageUrl,
        @PositiveOrZero Integer stockQty,
        Boolean active
) {}
