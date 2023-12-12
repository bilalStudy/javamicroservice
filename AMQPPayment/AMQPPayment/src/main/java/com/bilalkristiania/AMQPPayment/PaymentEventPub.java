package com.bilalkristiania.AMQPPayment;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventPub {
    private final AmqpTemplate amqpTemplate;
    private final String paymentExchange; // Rename to match your exchange name

    // Define your routing key as needed
    // You can adjust this based on your needs
    @Value("${amqp.routing.key.payment}")
    private String routingKey;

    public PaymentEventPub(final AmqpTemplate amqpTemplate,
                         @Value("${amqp.exchange.payment}") final String paymentExchange) {
        this.amqpTemplate = amqpTemplate;
        this.paymentExchange = paymentExchange;
    }



    public void sendPaymentEvent(final PaymentEvent paymentEvent) {
        PaymentEvent event = buildEvent(paymentEvent);
        amqpTemplate.convertAndSend(paymentExchange, routingKey, event);
    }

    private PaymentEvent buildEvent(final PaymentEvent inputEvent) {
        PaymentEvent event = new PaymentEvent();
        // Perform any necessary event transformation here
        event.setPaymentId(inputEvent.getPaymentId());
        event.setOrderId(inputEvent.getOrderId());
        event.setPaymentSuccessful(inputEvent.isPaymentSuccessful());
        event.setStatus(inputEvent.getStatus());
        event.setName(inputEvent.getName());
        event.setAmount(inputEvent.getAmount());
        return event;
    }
}
