package project.ee.models.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Must provide promotion name")
    private String promotion;

    @NotEmpty(message = "Must provide promotion type")
    private String promotionType;

    @NotEmpty(message = "Mut provide promo code")
    private String promoCode;

    @NotNull(message = "Must provide promotion en date")
    @FutureOrPresent(message = "Promotion end date must not be in the past")
    private LocalDate promotionEndDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Must provide a product for the promotion")
    private Product product;
}
