package com.bilalkristiania.AMQPPayment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class ReceiveOrderEvent {

    PaymentServiceImplementation paymentServiceImplementation;

    @Autowired
    public ReceiveOrderEvent(PaymentServiceImplementation paymentServiceImplementation) {
        this.paymentServiceImplementation = paymentServiceImplementation;
    }

    @RabbitListener(queues = "${amqp.queue.order}") // Replace with the actual queue name
    public void receiveOrderEvent(OrderEvent orderEvent) {
        log.info("Received Order Event in Payment Service: {}", orderEvent);
        PaymentResult paymentResult = paymentServiceImplementation.processOrderEvent(orderEvent);
        boolean paymentSuccessful = paymentResult.isPaymentSuccessful();
        // bankAccountAmount has no use anymore, but it exists in the case we want to send the bankaccountamount
        BigDecimal bankAccountAmount = paymentResult.getCurrentBalance();
        log.info("Test Log After Received Order Process in RecieveOrder{}", orderEvent);
        paymentServiceImplementation.sendRabbitPaymentEvent(orderEvent, paymentSuccessful);
    }
}
