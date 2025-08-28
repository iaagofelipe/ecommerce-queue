package com.store.poc.service;

import com.store.poc.models.OrderItem;
import com.store.poc.models.OutboxEvent;
import com.store.poc.models.PurchaseOrder;
import com.store.poc.models.dto.CreateOrderRequest;
import com.store.poc.models.dto.OrderEvent;
import com.store.poc.models.dto.OrderResponse;
import com.store.poc.repository.OutboxEventRepository;
import com.store.poc.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.store.poc.util.ToJsonUtil.toJson;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PurchaseOrderRepository repo;
    private final OutboxEventRepository outboxRepo;

    @Transactional
    public OrderResponse create(CreateOrderRequest req) {
        var order = new PurchaseOrder();
        order.setCustomerId(req.customerId());
        long total = 0L;
        for (var it : req.items()) {
            var item = OrderItem.builder()
                    .sku(it.sku())
                    .qty(it.qty())
                    .priceCents(it.priceCents())
                    .build();
            order.addItem(item);
            total += (long) it.qty() * it.priceCents();
        }
        order.setTotalCents(total);

        // persiste o pedido
        var saved = repo.save(order);

        // publica evento na SQS
        var payload = new OrderEvent(saved);
        var body = toJson(payload);

        outboxRepo.save(OutboxEvent.builder()
                .aggregateId(saved.getId())
                .eventType("ORDER_PLACED")
                .payload(body)
                .published(false)
                .build());

        return new OrderResponse(
                saved.getId(),
                saved.getCustomerId(),
                saved.getStatus(),
                saved.getTotalCents(),
                saved.getCreatedAt(),
                saved.getItems().stream()
                        .map(i -> new OrderResponse.Item(i.getSku(), i.getQty(), i.getPriceCents()))
                        .toList()
        );
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<OrderResponse> getById(UUID orderId) {
        return repo.findByIdFetchItems(orderId)
                .map(po -> new OrderResponse(
                        po.getId(),
                        po.getCustomerId(),
                        po.getStatus(),
                        po.getTotalCents(),
                        po.getCreatedAt(),
                        po.getItems().stream()
                                .map(i -> new OrderResponse.Item(i.getSku(), i.getQty(), i.getPriceCents()))
                                .toList()
                ));
    }

    public Page<OrderResponse> getAllByCustomerId(UUID customerId, Pageable pageable) {
        Page<PurchaseOrder> purchaseOrderPage = repo.findAllByCustomerId(customerId, pageable);

        return purchaseOrderPage.map(po -> new OrderResponse(
                po.getId(),
                po.getCustomerId(),
                po.getStatus(),
                po.getTotalCents(),
                po.getCreatedAt(),
                po.getItems().stream()
                        .map(i -> new OrderResponse.Item(i.getSku(), i.getQty(), i.getPriceCents()))
                        .toList()
        ));
    }
}
