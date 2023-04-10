package br.com.compassuol.challenge.order_microservice.dtos;

import br.com.compassuol.challenge.order_microservice.entities.ItemEntity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String cpf;
    private BigDecimal amount;
    private String orderStatus;
    private String paymentStatus;
    private List<ItemEntity> items;
}
