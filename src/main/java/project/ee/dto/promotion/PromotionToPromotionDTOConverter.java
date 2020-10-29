package project.ee.dto.promotion;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.models.models.Promotion;

@Component
public class PromotionToPromotionDTOConverter implements Converter<Promotion,PromotionDTO> {
    @Override
    public PromotionDTO convert(Promotion promotion) {
        if(promotion == null)
            return null;
        return PromotionDTO.builder()
                .promoCode(promotion.getPromoCode())
                .promotion(promotion.getPromotion())
                .promotionType(promotion.getPromotionType())
                .promotionEndDate(promotion.getPromotionEndDate())
                .productCode(promotion.getProduct().getProductCode())
                .build();
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}
