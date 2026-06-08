package com.etc.paymentms.producer;

import com.etc.paymentms.dto.PaymentProcessedEvent;

/**
 * Publishes payment processed events.
 */
public interface PaymentProcessedPublisher {

    /**
     * Publishes a payment processed event.
     *
     * @param event event to publish
     */
    void publish(PaymentProcessedEvent event);
}