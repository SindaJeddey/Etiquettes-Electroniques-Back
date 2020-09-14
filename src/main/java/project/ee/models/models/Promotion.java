package project.ee.models.models;

import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
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

    @NotEmpty
    private String promotion;

    @NotEmpty
    private String promotionType;

    private String promoCode;

    @NotEmpty
    @FutureOrPresent
    private LocalDate promotionEndDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Product product;
}
