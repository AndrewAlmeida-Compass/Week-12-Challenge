package br.com.compassuol.challenge.order_microservice.services.implementations;

import br.com.compassuol.challenge.order_microservice.DatabaseSequence;
import br.com.compassuol.challenge.order_microservice.dtos.ItemRequest;
import br.com.compassuol.challenge.order_microservice.dtos.ItemResponse;
import br.com.compassuol.challenge.order_microservice.dtos.OrderResponse;
import br.com.compassuol.challenge.order_microservice.entities.ItemEntity;
import br.com.compassuol.challenge.order_microservice.entities.OrderEntity;
import br.com.compassuol.challenge.order_microservice.repositories.ItemRepository;
import br.com.compassuol.challenge.order_microservice.services.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ItemServiceImp implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    private ModelMapper modelMapper;

    public ItemServiceImp(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemResponse registerItem(ItemRequest request) {
        ItemEntity item = modelMapper.map(request, ItemEntity.class);
        item.setId(this.generateSequence(OrderEntity.SEQUENCE_NAME));
        item.setAmount(this.formatCurrency(item.getAmount()));

        return modelMapper.map(itemRepository.save(item), ItemResponse.class);
    }


    @Override
    public ItemResponse updateItem(Long id, ItemRequest request) {
        ItemEntity itemEntity = itemRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No item was found"));

        ItemEntity item = modelMapper.map(request, ItemEntity.class);
        item.setId(id);
        item.setAmount(this.formatCurrency(item.getAmount()));

        return modelMapper.map(itemRepository.save(item), ItemResponse.class);
    }

    public Long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }

    public BigDecimal formatCurrency(BigDecimal number) {
        return new BigDecimal(String.valueOf(number)).setScale(2, RoundingMode.HALF_EVEN);
    }
}
