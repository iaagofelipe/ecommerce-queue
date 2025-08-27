package com.store.poc.service;

import com.store.poc.models.dto.OrderMessage;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.store.poc.util.FormatUtil.toSha256;
import static com.store.poc.util.ToJsonUtil.toJson;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final SqsTemplate sqsTemplate;

    @Value("${app.sqs.queue-name}")
    private String queueName;

    public void publish(OrderMessage msg) {
        String groupId = msg.orderId();
        String body = toJson(msg);
        String dedupId = toSha256(body);

        sqsTemplate.send(s -> s
                .queue(queueName)
                .payload(body)
                .messageGroupId(groupId)
                .messageDeduplicationId(dedupId));
    }

    public void publishRaw(String jsonBody, String messageGroupId, String dedupId) {
        sqsTemplate.send(s -> s
                .queue(queueName)
                .payload(jsonBody)
                .messageGroupId(messageGroupId)
                .messageDeduplicationId(dedupId));
    }
}
