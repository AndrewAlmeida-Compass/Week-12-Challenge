package br.com.compassuol.challenge.order_microservice.services;

import br.com.compassuol.challenge.order_microservice.dtos.ItemRequest;
import br.com.compassuol.challenge.order_microservice.dtos.ItemResponse;
import org.springframework.stereotype.Service;

@Service
public interface ItemService {
    ItemResponse registerItem(ItemRequest request);

    ItemResponse updateItem(Long id, ItemRequest request);

//    ItemResponse updateItem(Long id, ItemRequest request);
}