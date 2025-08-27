package com.store.poc.models.dto;

import com.store.poc.models.PurchaseOrder;

import java.util.UUID;

public record OrderEvent(
        UUID orderId,
        UUID customerId,
        long totalCents,
        String type
) {
    public OrderEvent(PurchaseOrder po) {
        this(po.getId(), po.getCustomerId(), po.getTotalCents(), "ORDER_PLACED");
    }
}