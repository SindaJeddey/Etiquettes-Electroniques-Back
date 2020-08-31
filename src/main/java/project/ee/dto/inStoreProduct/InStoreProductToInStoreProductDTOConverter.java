package project.ee.dto.inStoreProduct;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.dto.product.ProductToProductDTOConverter;
import project.ee.dto.store.StoreToStoreDTOConverter;
import project.ee.models.models.InStoreProduct;

@Component
public class InStoreProductToInStoreProductDTOConverter implements Converter<InStoreProduct,InStoreProductDTO> {

    private final ProductToProductDTOConverter toProductDTOConverter;
    private final StoreToStoreDTOConverter toStoreDTOConverter;

    public InStoreProductToInStoreProductDTOConverter(ProductToProductDTOConverter toProductDTOConverter, StoreToStoreDTOConverter toStoreDTOConverter) {
        this.toProductDTOConverter = toProductDTOConverter;
        this.toStoreDTOConverter = toStoreDTOConverter;
    }

    @Override
    public InStoreProductDTO convert(InStoreProduct inStoreProduct) {
        if(inStoreProduct == null)
            return null;
        return InStoreProductDTO.builder().id(inStoreProduct.getId())
                .product(toProductDTOConverter.convert(inStoreProduct.getProduct()))
                .store(toStoreDTOConverter.convert(inStoreProduct.getStore()))
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
