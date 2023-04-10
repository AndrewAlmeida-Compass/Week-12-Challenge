package br.com.compassuol.challenge.payment_microservice.messengers;

import br.com.compassuol.challenge.payment_microservice.entities.PaymentEntity;
import br.com.compassuol.challenge.payment_microservice.feign.UpdateOrderPayment;
import br.com.compassuol.challenge.payment_microservice.repositories.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import static java.lang.Long.parseLong;

@Service
@EnableRabbit
public class PaymentQueueListener {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    UpdateOrderPayment updateOrderPayment;


    @RabbitListener(queues = "#{@Payment}")
    public void getMessage(byte[] message){
        JSONObject obj = new JSONObject(new String(message));
        Long id = parseLong(obj.get("orderId").toString());
        String paymentStatus = obj.getString("paymentStatus");
        BigDecimal amount = new BigDecimal(obj.get("totalOrder").toString());

        PaymentEntity entity = new PaymentEntity(id, amount, paymentStatus);

        boolean entityExists = paymentRepository.existsById(entity.getOrderId());
        if (entityExists) {
            try {
                paymentRepository.save(entity);

                Map<String, String> body = new HashMap<>();
                body.put("id", String.valueOf(entity.getOrderId()));
                body.put("paymentStatus", entity.getPaymentStatus());

                updateOrderPayment.updateOrderPayment(body);
            } catch (Exception e){
                rabbitTemplate.convertAndSend("PaymentResponse", e.getMessage());
            }
        } else {
            rabbitTemplate.convertAndSend("PaymentResponse", String.format("There's no order with id %d", id));
        }
    }
}
