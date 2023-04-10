package br.com.compassuol.challenge.payment_microservice.repositories;

import br.com.compassuol.challenge.payment_microservice.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
