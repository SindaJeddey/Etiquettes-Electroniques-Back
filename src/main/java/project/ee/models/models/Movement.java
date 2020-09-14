package project.ee.models.models;

import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
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

    @NotEmpty
    private String type;

    @FutureOrPresent
    private LocalDate movementDate;

    private String movementCode;

    @NotNull
    @Positive
    private Long quantity;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id")
    private InStoreProduct product;
}
