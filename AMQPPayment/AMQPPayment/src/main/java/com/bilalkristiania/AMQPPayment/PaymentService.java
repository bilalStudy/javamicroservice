package com.bilalkristiania.AMQPPayment;

import jakarta.persistence.criteria.Order;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentResult processOrderEvent(OrderEvent orderEvent);
    //boolean processPayment(double amount);

    PaymentResult processPayment(OrderEvent orderEvent);

    double checkBalance();

    Payment getPaymentById(Long id);

    Payment savePayment(Payment payment);

    Payment getInternalPayment(Long id);

    Payment saveInternalPayment(Payment payment);

    void sendRabbitPaymentEvent(OrderEvent orderEvent, boolean paymentSuccessful);
}