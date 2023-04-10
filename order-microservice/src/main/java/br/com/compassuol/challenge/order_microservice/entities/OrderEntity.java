package br.com.compassuol.challenge.order_microservice.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Document(collection = "orders")
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class OrderEntity implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "orders_id_sequence";

    @Id
    private Long id;

    @NotBlank(message = "CPF can't be blank or null.")
    @Size(min = 11, max = 11, message = "CPF must contain 11 digits.")
    private String cpf;

    @NotNull(message = "Amount can't be blank or null.")
    private BigDecimal amount;

    private String orderStatus;

    private String paymentStatus;

    @NotNull(message = "Order items can't be null.")
    @NotEmpty(message = "Order items can't be empty.")
    @DBRef
    private List<ItemEntity> items;
}

