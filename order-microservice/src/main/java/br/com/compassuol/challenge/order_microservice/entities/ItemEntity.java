package br.com.compassuol.challenge.order_microservice.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "items")
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class ItemEntity implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "items_id_sequence";

    @Id
    private Long id;

    @NotBlank(message = "Name can't be blank or null.")
    private String name;

    @NotBlank(message = "Description can't be blank or null.")
    private String description;

    @NotNull(message = "Creation date can't be blank or null.")
    private LocalDate creationDate;

    @NotNull(message = "Amount can't be blank or null.")
    private BigDecimal amount;
}
