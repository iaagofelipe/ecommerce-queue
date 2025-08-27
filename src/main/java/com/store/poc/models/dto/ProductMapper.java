package com.store.poc.models.dto;

import com.store.poc.models.Product;

public final class ProductMapper {
    private ProductMapper(){}

    public static ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(), p.getSku(), p.getName(), p.getDescription(),
                p.getPriceCents(), p.getImageUrl(), p.getStockQty(),
                Boolean.TRUE.equals(p.getActive()),
                p.getCreatedAt(), p.getUpdatedAt()
        );
    }
}
