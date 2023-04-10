package br.com.compassuol.challenge.order_microservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "order-microservice", url = "http://localhost:8081/api/payment")
public interface Payments {
    @PostMapping("/new")
    void newOrder(Map body);

    @GetMapping("/{id}")
    void deletePayment(@PathVariable("id") Long id);

}
