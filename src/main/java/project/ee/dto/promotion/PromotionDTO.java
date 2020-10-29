package project.ee.dto.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {
    private String promotion;

    private String promotionType;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate promotionEndDate;

    private String productCode;

    private String promoCode;
}
