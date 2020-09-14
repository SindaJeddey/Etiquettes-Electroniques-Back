package project.ee.dto.promotion;

import lombok.*;
import project.ee.dto.product.ProductDTO;
import project.ee.models.models.Product;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {
    private String promotion;

    private String promotionType;

    private LocalDate promotionEndDate;

    private String productCode;

    private String promoCode;
}
