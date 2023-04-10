package br.com.compassuol.challenge.order_microservice.controllers;

import br.com.compassuol.challenge.order_microservice.dtos.OrderRequest;
import br.com.compassuol.challenge.order_microservice.dtos.OrderResponse;
import br.com.compassuol.challenge.order_microservice.entities.OrderEntity;
import br.com.compassuol.challenge.order_microservice.feign.Payments;
import br.com.compassuol.challenge.order_microservice.repositories.OrderRepository;
import br.com.compassuol.challenge.order_microservice.services.implementations.OrderServiceImp;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    OrderServiceImp orderServiceImp;

    @Autowired
    Payments payments;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin rabbitAdmin;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll(
            @RequestParam(name = "sort", required = false) String sortAmount,
            @RequestParam(name = "cpf", required = false) String filterByCPF){

        return ResponseEntity.ok(orderServiceImp.getAll(sortAmount, filterByCPF));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> registerOrder(@Valid @RequestBody OrderRequest request){
        OrderResponse orderResponse = orderServiceImp.registerOrder(request);

        Map<String, String> body = new HashMap<>();
        body.put("id", String.valueOf(orderResponse.getId()));
        body.put("amount", String.valueOf(orderResponse.getAmount()));

        payments.newOrder(body);

        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        OrderResponse orderResponse = orderServiceImp.getById(id);
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderServiceImp.updateOrder(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderServiceImp.deleteOrder(id));
    }


    @PostMapping("/updatepayment")
    public void updatePayment(@RequestBody Map body){
        Long id = parseLong(body.get("id").toString());
        String paymentStatus = body.get("paymentStatus").toString().toUpperCase();

        OrderEntity entity = orderRepository.findById(id).get();

        List<String> availableStatus = new ArrayList<>(List.of("PROCESSING","REJECTED","APPROVED"));

        if(availableStatus.contains(paymentStatus)){
            entity.setPaymentStatus(paymentStatus);
        }

        if (paymentStatus.equals("PROCESSING")) {
            entity.setOrderStatus("IN_PROGRESS");
        } else if (paymentStatus.equals("REJECTED")) {
            entity.setOrderStatus("CANCELED");
        } else if (paymentStatus.equals("APPROVED")) {
            entity.setOrderStatus("FINISHED");
        }

        orderRepository.save(entity);

        rabbitAdmin.declareQueue(new Queue("PaymentResponse"));

        rabbitTemplate.convertAndSend("PaymentResponse", String.format(
                "The order with id %d was updated wit the payment status %s and order status %s",
                id, paymentStatus, entity.getOrderStatus()));
    }

}
