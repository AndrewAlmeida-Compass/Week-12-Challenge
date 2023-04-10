package br.com.compassuol.challenge.payment_microservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;

@Entity
@Data
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity implements Serializable {
    @Id
    @NotNull(message = "Id can't be blank or null.")
    private Long orderId;

    @NotNull(message = "Order total can't be blank or null.")
    private BigDecimal totalOrder;

    @NotBlank(message = "Payment status can't be blank or null.")
    @Pattern(regexp = "^(PROCESSING)$|^(REJECTED)$|^(APPROVED)$",
            message = "Wrong payment status, your options are [PROCESSING, REJECTED, APPROVED]",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String paymentStatus;
}
