package br.com.compassuol.challenge.payment_microservice.controllers;

import br.com.compassuol.challenge.payment_microservice.entities.PaymentEntity;
import br.com.compassuol.challenge.payment_microservice.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import static java.lang.Long.parseLong;

@RestController
@RequestMapping("api/payment")
public class PaymentController {
    @Autowired
    PaymentRepository paymentRepository;

    @PostMapping("/new")
    public void save(@RequestBody Map body){
        Long id = parseLong(body.get("id").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString()).setScale(2, RoundingMode.HALF_EVEN);
        PaymentEntity entity = new PaymentEntity(id, amount, "PROCESSING");

        paymentRepository.save(entity);
    }

    @GetMapping("/{id}")
    public void deletePayment(@PathVariable Long id){
        paymentRepository.deleteById(id);
    }

    @GetMapping
    public List<PaymentEntity> getAllPayments(){
        List<PaymentEntity> allPayments = paymentRepository.findAll();

        if (allPayments.stream().count() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No payment was found.");
        } else {
            return allPayments;
        }
    }
}
