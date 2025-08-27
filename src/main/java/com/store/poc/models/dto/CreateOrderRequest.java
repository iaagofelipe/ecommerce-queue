package com.store.poc.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotEmpty List<Item> items
) {
    public record Item(
            @NotBlank String sku,
            @Positive int qty,
            @Positive long priceCents
    ) {}
}
