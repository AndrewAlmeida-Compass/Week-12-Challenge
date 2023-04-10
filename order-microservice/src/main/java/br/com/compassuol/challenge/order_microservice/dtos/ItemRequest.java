package br.com.compassuol.challenge.order_microservice.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ItemRequest {
    private String name;
    private String description;
    private LocalDate creationDate;
    private BigDecimal amount;
}
