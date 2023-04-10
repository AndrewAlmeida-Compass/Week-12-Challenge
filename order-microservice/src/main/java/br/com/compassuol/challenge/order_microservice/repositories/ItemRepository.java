package br.com.compassuol.challenge.order_microservice.repositories;

import br.com.compassuol.challenge.order_microservice.entities.ItemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends MongoRepository<ItemEntity, Long> {

}
