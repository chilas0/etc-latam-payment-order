package com.etc.paymentms.consumer;

import com.etc.paymentms.dto.OrderPlacedEvent;
import com.etc.paymentms.dto.PaymentProcessedEvent;
import com.etc.paymentms.producer.PaymentProcessedPublisher;
import com.etc.paymentms.service.PaymentProcessorService;
import com.etc.paymentms.service.RsaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumes order placed events and simulates payment processing.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedConsumer {

    private final PaymentProcessorService paymentProcessorService;

    private final PaymentProcessedPublisher paymentProcessedPublisher;

    private final RsaService rsaService;

    /**
     * Consumes order placed events.
     *
     * @param event received order event
     */
    @KafkaListener(
            topics = "order-placed",
            groupId = "payment-ms-group"
    )
    public void consume(
            OrderPlacedEvent event) {

        log.info(
                "Received order event: {}",
                event
        );

        String cardNumber =
                rsaService.decrypt(
                        event.getEncryptedCardNumber()
                );

        if (cardNumber == null || cardNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Invalid card number"
            );
        }

        log.info(
                "Card successfully decrypted. Last four digits: {}",
                cardNumber.substring(
                        cardNumber.length() - 4
                )
        );

        boolean approved =
                paymentProcessorService.processPayment();

        PaymentProcessedEvent paymentEvent =
                PaymentProcessedEvent.builder()
                        .orderId(event.getOrderId())
                        .status(
                                approved
                                        ? "PAID"
                                        : "FAILED_PAYMENT"
                        )
                        .build();

        paymentProcessedPublisher.publish(
                paymentEvent
        );
    }
}