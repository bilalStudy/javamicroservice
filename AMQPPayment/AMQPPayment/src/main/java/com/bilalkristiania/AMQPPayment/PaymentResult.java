package com.bilalkristiania.AMQPPayment;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResult {
    private boolean paymentSuccessful;
    private BigDecimal currentBalance;
}
