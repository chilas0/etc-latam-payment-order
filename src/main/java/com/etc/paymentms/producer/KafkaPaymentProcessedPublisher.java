package com.etc.paymentms.producer;

import com.etc.paymentms.dto.PaymentProcessedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka implementation for publishing payment processed events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaPaymentProcessedPublisher
        implements PaymentProcessedPublisher {

    private final KafkaTemplate<
            String,
            PaymentProcessedEvent> kafkaTemplate;

    /**
     * Publishes an event to the payment-processed topic.
     *
     * @param event event to publish
     */
    @Override
    public void publish(
            PaymentProcessedEvent event) {

        kafkaTemplate.send(
                "payment-processed",
                event
        ).whenComplete((result, ex) -> {

            if (ex != null) {

                log.error(
                        "Error sending payment processed event",
                        ex
                );

            } else {

                log.info(
                        "Payment event sent. Topic={}, Partition={}, Offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()
                );
            }
        });
    }
}