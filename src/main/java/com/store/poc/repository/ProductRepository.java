package com.store.poc.repository;

import com.store.poc.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findBySku(String sku);

    @Query("""
              select p from Product p
              where (:q is null or
                    lower(p.name) like lower(concat('%', COALESCE(:q, ''), '%')) or
                    lower(p.description) like lower(concat('%', COALESCE(:q, ''), '%')) or
                    lower(p.sku) like lower(concat('%', COALESCE(:q, ''), '%')))
                and (:active is null or p.active = :active)
            """)
    Page<Product> search(@Param("q") String q, @Param("active") Boolean active, Pageable pageable);

    Page<Product> findAllByActive(Boolean active, Pageable pageable);

    @Query("select p from Product p where :active is null or p.active = :active")
    Page<Product> findAllByActiveFilter(@Param("active") Boolean active, Pageable pageable);
}
