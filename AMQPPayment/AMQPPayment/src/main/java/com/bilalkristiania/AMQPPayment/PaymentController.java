package com.bilalkristiania.AMQPPayment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentRepository paymentRepository;

    PaymentController(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/payment/{id}")
    public Payment getOrderById(@PathVariable Long id){
        return paymentRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Didnt find PaymentAccount" + id));
    }

    @PostMapping("/payment")
    public Payment newAccount(@RequestBody Payment payment){
        return paymentRepository.save(payment);
    }

}
