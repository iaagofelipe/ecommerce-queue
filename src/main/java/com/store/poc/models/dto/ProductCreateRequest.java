package com.store.poc.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(
        @NotBlank @Size(max = 64) String sku,
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Size(max = 2000) String description,
        @Positive Long priceCents,
        @NotBlank @Size(max = 512) String imageUrl,
        @PositiveOrZero Integer stockQty,
        Boolean active
) {}
