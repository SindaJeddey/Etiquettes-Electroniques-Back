package project.ee.dto.inStoreProduct;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.dto.product.ProductDTOToProductConverter;
import project.ee.dto.store.StoreDTOToStoreConverter;
import project.ee.models.models.InStoreProduct;

@Component
public class InStoreProductDTOToInStoreProductConverter implements Converter<InStoreProductDTO, InStoreProduct> {

    private final ProductDTOToProductConverter toProductConverter;
    private final StoreDTOToStoreConverter toStoreConverter;

    public InStoreProductDTOToInStoreProductConverter(ProductDTOToProductConverter toProductConverter, StoreDTOToStoreConverter toStoreConverter) {
        this.toProductConverter = toProductConverter;
        this.toStoreConverter = toStoreConverter;
    }

    @Override
    public InStoreProduct convert(InStoreProductDTO inStoreProductDTO) {
        if(inStoreProductDTO == null)
            return null;
        return InStoreProduct.builder()
                .inStoreProductCode(inStoreProductDTO.getInStoreProductCode())
                .product(toProductConverter.convert(inStoreProductDTO.getProduct()))
                .store(toStoreConverter.convert(inStoreProductDTO.getStore()))
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
