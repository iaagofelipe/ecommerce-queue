package com.store.poc.service;

import com.store.poc.models.Product;
import com.store.poc.models.dto.ProductCreateRequest;
import com.store.poc.models.dto.ProductMapper;
import com.store.poc.models.dto.ProductResponse;
import com.store.poc.models.dto.ProductUpdateRequest;
import com.store.poc.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    public Page<ProductResponse> search(String q, Boolean active, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return repo.search(emptyToNull(q), active, pageable).map(ProductMapper::toResponse);
    }

    public Optional<ProductResponse> getById(UUID id) {
        return repo.findById(id).map(ProductMapper::toResponse);
    }

    public Optional<ProductResponse> getBySku(String sku) {
        return repo.findBySku(sku).map(ProductMapper::toResponse);
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest req) {
        var p = Product.builder()
                .sku(req.sku())
                .name(req.name())
                .description(req.description())
                .priceCents(req.priceCents())
                .imageUrl(req.imageUrl())
                .stockQty(req.stockQty())
                .active(req.active() == null || req.active())
                .build();
        var saved = repo.save(p);
        return ProductMapper.toResponse(saved);
    }

    @Transactional
    public Optional<ProductResponse> update(UUID id, ProductUpdateRequest req) {
        return repo.findById(id).map(p -> {
            if (req.name() != null) p.setName(req.name());
            if (req.description() != null) p.setDescription(req.description());
            if (req.priceCents() != null) p.setPriceCents(req.priceCents());
            if (req.imageUrl() != null) p.setImageUrl(req.imageUrl());
            if (req.stockQty() != null) p.setStockQty(req.stockQty());
            if (req.active() != null) p.setActive(req.active());
            return ProductMapper.toResponse(repo.save(p));
        });
    }

    @Transactional
    public boolean delete(UUID id) {
        return repo.findById(id).map(p -> {
            repo.delete(p);
            return true;
        }).orElse(false);
    }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
