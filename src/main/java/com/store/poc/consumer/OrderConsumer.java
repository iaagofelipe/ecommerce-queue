package com.store.poc.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Headers;
import io.awspring.cloud.sqs.annotation.SqsListener;

import java.util.Map;

@Slf4j
@Component
public class OrderConsumer {

    @SqsListener("${app.sqs.queue-name}")
    public void onMessage(String payload, @Headers Map<String, Object> headers) {
        // headers inclui MessageId, ReceiptHandle, MessageGroupId, etc.
        log.info("SQS RECEIVED: payload={}, headers={}", payload, headers);
        // Se este método concluir sem lançar exceção, a mensagem é deletada da fila
    }

}
