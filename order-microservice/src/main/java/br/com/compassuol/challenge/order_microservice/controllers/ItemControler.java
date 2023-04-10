package br.com.compassuol.challenge.order_microservice.controllers;

import br.com.compassuol.challenge.order_microservice.dtos.ItemRequest;
import br.com.compassuol.challenge.order_microservice.dtos.ItemResponse;
import br.com.compassuol.challenge.order_microservice.services.implementations.ItemServiceImp;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/item")
public class ItemControler {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ItemServiceImp itemServiceImp;

    @PostMapping
    public ResponseEntity<ItemResponse> registerItem(@Valid @RequestBody ItemRequest request){
        return ResponseEntity.ok(itemServiceImp.registerItem(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(itemServiceImp.updateItem(id, request));
    }
}
