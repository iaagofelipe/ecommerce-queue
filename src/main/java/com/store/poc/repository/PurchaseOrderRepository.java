package com.store.poc.repository;

import com.store.poc.models.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
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

    @Query("""
       select distinct po
       from PurchaseOrder po
       left join fetch po.items
       where po.customerId = :customerId
       """)
    List<PurchaseOrder> findAllByCustomerIdFetchItems(UUID customerId);}
