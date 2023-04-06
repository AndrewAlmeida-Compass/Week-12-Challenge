package br.com.compassuol.challenge.order_microservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Document(collection = "order")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Order implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "orders_id_sequence";

    @Id
    private Long id;

    @NotBlank(message = "CPF can't be blank or null")
    @Size(min = 11, max = 11, message = "CPF must contain 11 digits")
    private String cpf;

    @NotBlank(message = "Name can't be blank or null")
    private String name;
}
//        "id": 1,
//        "cpf": "12345678911",
//        "items": [
//        {
//        "id": 1,
//        "name": "item name",
//        "description": "item description",
//        "creationDate": "yyyy/MM/dd",
//        "expirationDate": "yyyy/MM/dd",
//        "amount": "19.99",
//        "offer": "item on offer"
//        }
//        ],
//        "amount": "99.99",
//        "orderStatus": "in progress",
//        "paymentStatus": "approved"
//        }

