package br.com.compassuol.challenge.order_microservice.controllers;

import br.com.compassuol.challenge.order_microservice.DatabaseSequence;
import br.com.compassuol.challenge.order_microservice.dtos.OrderRequest;
import br.com.compassuol.challenge.order_microservice.dtos.OrderResponse;
import br.com.compassuol.challenge.order_microservice.services.implementations.OrderServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
@RequestMapping("api/order")
public class controler {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    OrderServiceImp orderServiceImp;

    @PostMapping
    public ResponseEntity<OrderResponse> save(@Valid @RequestBody OrderRequest request){
        return ResponseEntity.ok(orderServiceImp.save(request));
    }

}
