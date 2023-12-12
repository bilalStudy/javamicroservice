package com.bilalkristiania.AMQPPayment;


import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImplementation implements PaymentService{

    private double balance = 4000000.0;

    private Payment payment;

    private final PaymentRepository paymentRepository;

    private final PaymentEventPub paymentEventPub;


    // remember to check for if user/order exists.
    // might need to parse the Double to BigDecimal instead
    @Override
    public PaymentResult processOrderEvent(OrderEvent orderEvent) {
        double orderAmount = Double.parseDouble(orderEvent.getAmount());
        log.info("Processing Order Event: {}", orderEvent);
        PaymentResult paymentResult = processPayment(orderEvent);
        boolean paymentSuccessful = paymentResult.isPaymentSuccessful();
        BigDecimal currentBalance = paymentResult.getCurrentBalance();
        log.info("Processed Order Event: {}", orderEvent);
        return new PaymentResult(paymentSuccessful, currentBalance);
    }


    @Override
    public PaymentResult processPayment(OrderEvent orderEvent) {
        Long paymentId = 1L; // Adjust the ID based on your setup
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

        Payment payment = optionalPayment.orElseGet(() -> {
            // If payment doesn't exist, create a new one
            Payment newPayment = new Payment();
            newPayment.setBalance(BigDecimal.valueOf(4000000.0));
            return paymentRepository.save(newPayment);
        });

        double orderAmount = Double.parseDouble(orderEvent.getAmount());
        BigDecimal currentBalance = payment.getBalance();

        boolean paymentSuccessful = false;

        if (currentBalance.compareTo(BigDecimal.valueOf(orderAmount)) >= 0) {
            // Sufficient funds, update the balance
            payment.setBalance(currentBalance.subtract(BigDecimal.valueOf(orderAmount)));
            paymentSuccessful = true;
        }

        logPaymentStatus(paymentSuccessful, currentBalance);
        orderEvent.setStatus(paymentSuccessful ? "goodFunds" : "badFunds");

        // Save the updated payment entity
        paymentRepository.save(payment);

        return new PaymentResult(paymentSuccessful, currentBalance);
    }


    private void logPaymentStatus(boolean paymentSuccessful, BigDecimal currentBalance) {
        if (paymentSuccessful) {
            log.info("Payment successful. Remaining balance: {}", currentBalance);
        } else {
            log.warn("Insufficient funds for payment. Current balance: {}", currentBalance);
        }
    }

    @Override
    public double checkBalance() {
        if (payment == null) {
            return 0.0;  // No account, return 0 balance
        }
        return payment.getBalance().doubleValue();
    }

    @Override
    public Payment getPaymentById(Long id) {
        return null;
    }

    @Override
    public Payment savePayment(Payment payment) {
        return null;
    }

    @Override
    public Payment getInternalPayment(Long id) {
        return payment;
    }

    @Override
    public Payment saveInternalPayment(Payment newPayment) {
        if (payment != null) {
            // Update the existing payment entity
            payment.setBalance(newPayment.getBalance());
        } else {
            payment = newPayment;
        }
        return payment;
    }

    @Override
    public void sendRabbitPaymentEvent(OrderEvent orderEvent, boolean paymentSuccessful) {
        // save payment if needed

        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setPaymentId(1L);
        paymentEvent.setOrderId(orderEvent.getId());
        paymentEvent.setPaymentSuccessful(paymentSuccessful);
        paymentEvent.setStatus(orderEvent.getStatus());
        paymentEvent.setName(orderEvent.getName());
        paymentEvent.setAmount(orderEvent.getAmount());

        paymentEventPub.sendPaymentEvent(paymentEvent);
    }


}
