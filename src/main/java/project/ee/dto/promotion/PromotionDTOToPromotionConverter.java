package project.ee.dto.promotion;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.models.models.Promotion;

@Component
public class PromotionDTOToPromotionConverter implements Converter<PromotionDTO, Promotion> {
    @Override
    public Promotion convert(PromotionDTO promotionDTO) {
        if(promotionDTO == null)
            return null;
        return Promotion.builder()
                .promoCode(promotionDTO.getPromoCode())
                .promotion(promotionDTO.getPromotion())
                .promotionType(promotionDTO.getPromotionType())
                .promotionEndDate(promotionDTO.getPromotionEndDate())
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
