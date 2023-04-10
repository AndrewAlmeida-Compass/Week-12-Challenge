package br.com.compassuol.challenge.order_microservice.services.implementations;

import br.com.compassuol.challenge.order_microservice.config.DatabaseSequence;
import br.com.compassuol.challenge.order_microservice.dtos.OrderRequest;
import br.com.compassuol.challenge.order_microservice.dtos.OrderResponse;
import br.com.compassuol.challenge.order_microservice.entities.ItemEntity;
import br.com.compassuol.challenge.order_microservice.entities.OrderEntity;
import br.com.compassuol.challenge.order_microservice.feign.Payments;
import br.com.compassuol.challenge.order_microservice.repositories.ItemRepository;
import br.com.compassuol.challenge.order_microservice.repositories.OrderRepository;
import br.com.compassuol.challenge.order_microservice.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    Payments payments;

    public OrderServiceImp(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse registerOrder(OrderRequest request) {
        OrderEntity order = modelMapper.map(request, OrderEntity.class);
        order.setId(this.generateSequence(OrderEntity.SEQUENCE_NAME));

        if (order.getOrderStatus() != null){
            order.setOrderStatus(order.getOrderStatus().toUpperCase());
        }

        List<ItemEntity> items = order.getItems();

        BigDecimal orderAmount = new BigDecimal("0.00");

        for (ItemEntity item: items) {
            item.setId(this.generateSequence(ItemEntity.SEQUENCE_NAME));
            if (item.getAmount() != null){
                item.setAmount(this.formatCurrency(item.getAmount()));
            }
            itemRepository.save(item);
            orderAmount = orderAmount.add(new BigDecimal(String.valueOf(item.getAmount())));
        }

        order.setItems(items);
        order.setAmount(orderAmount);
        order.setPaymentStatus("PROCESSING");
        order.setOrderStatus("IN_PROGRESS");

        OrderEntity orderEntity = orderRepository.save(order);

        return modelMapper.map(orderEntity, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getAll(String sortAmount, String filterByCPF) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCpf(filterByCPF);

        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase();
        Example<OrderEntity> example = Example.of(orderEntity, matcher);

        Sort.Direction direction = Sort.DEFAULT_DIRECTION;
        ArrayList<String> sortOptions = new ArrayList<String>(List.of("ASC", "DESC"));

        if (sortAmount != null) {
            String formatedSortByOption = sortAmount.toUpperCase();

            boolean optionExist = sortOptions.contains(formatedSortByOption);

            if (optionExist) {
                direction = Sort.Direction.valueOf(formatedSortByOption);
            }
        }

        Sort sortByAmount = Sort.by(direction, "amount");

        List<OrderEntity> orderEntities = orderRepository.findAll(example, sortByAmount);

        List<OrderResponse> allOrdersEntities = orderEntities
                .stream()
                .map(entity -> modelMapper.map(entity, OrderResponse.class))
                .collect(Collectors.toList());

        if (orderEntities.stream().count() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order was found!");
        } else {
            return allOrdersEntities;
        }
    }

    @Override
    public OrderResponse getById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No order was found"));
        return modelMapper.map(orderEntity, OrderResponse.class);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No order was found"));

        OrderEntity order = modelMapper.map(request, OrderEntity.class);
        order.setId(id);
        order.setPaymentStatus(orderEntity.getPaymentStatus().toUpperCase());
        order.setOrderStatus(orderEntity.getOrderStatus().toUpperCase());

        List<ItemEntity> items = order.getItems();

        BigDecimal orderAmount = new BigDecimal("0.00");

        for (ItemEntity item: items) {
            item.setId(item.getId());
            if (item.getAmount() != null){
                item.setAmount(this.formatCurrency(item.getAmount()));
            }
            itemRepository.save(item);
            orderAmount = orderAmount.add(new BigDecimal(String.valueOf(item.getAmount())));
        }

        order.setItems(items);
        order.setAmount(orderAmount);

        OrderEntity updatedOrder = orderRepository.save(order);

        return modelMapper.map(updatedOrder, OrderResponse.class);
    }

    @Override
    public String deleteOrder(Long id) {

        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No order was found"));

        if (orderEntity.getPaymentStatus().equalsIgnoreCase("APPROVED")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete an order with an approved payment.");
        } else {
            orderRepository.deleteById(id);

            payments.deletePayment(id);

            return String.format("Order %d was deleted.", id);
        }
    }

    public Long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }

    public BigDecimal formatCurrency(BigDecimal number) {
        BigDecimal bd = new BigDecimal(String.valueOf(number)).setScale(2, RoundingMode.HALF_EVEN);
        return bd;
    }
}
