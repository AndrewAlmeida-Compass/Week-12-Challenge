package br.com.compassuol.challenge.order_microservice.dtos;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String name;
    private String cpf;
}
