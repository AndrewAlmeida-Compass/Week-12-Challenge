package br.com.compassuol.challenge.order_microservice.services;

import br.com.compassuol.challenge.order_microservice.dtos.OrderRequest;
import br.com.compassuol.challenge.order_microservice.dtos.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderResponse save(OrderRequest request);
}