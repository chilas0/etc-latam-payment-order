package com.etc.paymentms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Simulates payment processing.
 */
@Service
@Slf4j
public class PaymentProcessorService {

    private final Random random = new Random();

    /**
     * Simulates a payment approval or rejection.
     *
     * @return true if payment is approved
     */
    public boolean processPayment() {

        boolean approved =
                random.nextBoolean();

        log.info(
                "Payment processing result: {}",
                approved ? "APPROVED" : "REJECTED"
        );

        return approved;
    }
}