package br.com.compassuol.challenge.order_microservice.services;

import br.com.compassuol.challenge.order_microservice.dtos.OrderRequest;
import br.com.compassuol.challenge.order_microservice.dtos.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponse registerOrder(OrderRequest request);

    List<OrderResponse> getAll(String sortAmount, String filterByCPF);

    OrderResponse getById(Long id);

    OrderResponse updateOrder(Long id, OrderRequest request);

    String deleteOrder(Long id);
}