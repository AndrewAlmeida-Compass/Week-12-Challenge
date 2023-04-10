package br.com.compassuol.challenge.payment_microservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Map;

@FeignClient(name = "payment-microservice", url = "http://localhost:8080/api/order/updatepayment")
public interface UpdateOrderPayment {
    @PostMapping
    void updateOrderPayment(Map body);
}
