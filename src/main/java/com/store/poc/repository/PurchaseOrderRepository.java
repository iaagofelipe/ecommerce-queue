package com.store.poc.repository;

import com.store.poc.models.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {

    @Query("""
            
               select distinct po
            from PurchaseOrder po
            left join fetch po.items
            where po.id = :id
            """)
    Optional<PurchaseOrder> findByIdFetchItems(UUID id);

    /**
     * Busca todos os PurchaseOrders de um cliente de forma paginada.
     *
     * @EntityGraph é usado para resolver o problema de N+1 select, garantindo
     * que os 'items' sejam carregados na mesma consulta (similar ao JOIN FETCH).
     * O Spring Data JPA gerencia a paginação de forma eficiente.
     */
    @EntityGraph(attributePaths = {"items"})
    Page<PurchaseOrder> findAllByCustomerId(UUID customerId, Pageable pageable);
}
