package com.store.poc.relayer;

import com.store.poc.repository.OutboxEventRepository;
import com.store.poc.util.FormatUtil;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayer {

    private final OutboxEventRepository repo;
    private final SqsTemplate sqsTemplate;

    @Value("${app.sqs.queue-name}")
    private String queueName;

    @Scheduled(fixedDelay = 1000) // a cada 1s busca itens pendentes
    public void publishPending() {
        var batch = repo.findTop50ByPublishedFalseOrderByCreatedAtAsc();
        for (var evt : batch) {
            try {
                var groupId = evt.getAggregateId().toString();   // orderId
                var body = evt.getPayload();
                var dedupId = FormatUtil.toSha256(body);

                sqsTemplate.send(s -> s
                        .queue(queueName)
                        .payload(body)
                        .messageGroupId(groupId)
                        .messageDeduplicationId(dedupId));

                evt.setPublished(true);
                repo.save(evt);
                log.info("Outbox {} -> publicado no SQS (orderId={})", evt.getId(), groupId);

            } catch (Exception e) {
                // NÃO marca como publicado; o próximo ciclo tenta de novo
                log.warn("Falha ao publicar outbox {}: {}", evt.getId(), e.getMessage());
            }
        }
    }

}
