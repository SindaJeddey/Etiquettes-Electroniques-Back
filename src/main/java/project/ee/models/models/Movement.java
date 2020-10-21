package project.ee.models.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Must provide a movement type")
    private String type;

    @NotNull(message = "Must provide a movement date")
    private LocalDate movementDate;

    @NotNull(message = "Must provide a movement code")
    private String movementCode;

    @NotNull(message = "Must provide a quantity for the movement")
    @Positive(message = "Quantity must be positive")
    private Long quantity;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id")
    private InStoreProduct product;
}
