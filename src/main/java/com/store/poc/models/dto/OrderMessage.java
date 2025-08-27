package com.store.poc.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record OrderMessage(
        @NotBlank String orderId,
        @NotBlank String sku,
        @Positive int qty
) {
}
