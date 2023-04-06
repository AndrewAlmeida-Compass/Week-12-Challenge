package br.com.compassuol.challenge.order_microservice.services.implementations;

import br.com.compassuol.challenge.order_microservice.DatabaseSequence;
import br.com.compassuol.challenge.order_microservice.dtos.OrderRequest;
import br.com.compassuol.challenge.order_microservice.dtos.OrderResponse;
import br.com.compassuol.challenge.order_microservice.entities.Order;
import br.com.compassuol.challenge.order_microservice.repositories.OrderRepository;
import br.com.compassuol.challenge.order_microservice.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class OrderServiceImp implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    private ModelMapper modelMapper;

    public OrderServiceImp(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse save(OrderRequest request) {
        Order order = modelMapper.map(request, Order.class);
        order.setId(this.generateSequence(Order.SEQUENCE_NAME));
        Order save = orderRepository.save(order);

        OrderResponse saved = modelMapper.map(save, OrderResponse.class);

        return saved;
    }
    public Long generateSequence(String seqName) {

        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
