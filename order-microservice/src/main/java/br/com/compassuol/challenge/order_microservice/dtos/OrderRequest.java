package br.com.compassuol.challenge.order_microservice.dtos;

import lombok.Data;

@Data
public class OrderRequest {
    private String name;
    private String cpf;
}
