package br.com.compassuol.challenge.order_microservice.dtos;

import br.com.compassuol.challenge.order_microservice.entities.ItemEntity;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private String cpf;
    private List<ItemEntity> items;
}
