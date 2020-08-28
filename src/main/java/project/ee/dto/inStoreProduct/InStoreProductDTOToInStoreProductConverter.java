package project.ee.dto.inStoreProduct;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.dto.product.ProductDTOToProductConverter;
import project.ee.dto.store.StoreDTOToStoreConverter;
import project.ee.dto.tag.TagDTOToTagConverter;
import project.ee.models.models.InStoreProduct;

@Component
public class InStoreProductDTOToInStoreProductConverter implements Converter<InStoreProductDTO, InStoreProduct> {

    private final StoreDTOToStoreConverter toStoreConverter;
    private final ProductDTOToProductConverter toProductConverter;
    private final TagDTOToTagConverter toTagConverter;

    public InStoreProductDTOToInStoreProductConverter(StoreDTOToStoreConverter toStoreConverter,
                                                      ProductDTOToProductConverter toProductConverter,
                                                      TagDTOToTagConverter toTagConverter) {
        this.toStoreConverter = toStoreConverter;
        this.toProductConverter = toProductConverter;
        this.toTagConverter = toTagConverter;
    }

    @Override
    public InStoreProduct convert(InStoreProductDTO inStoreProductDTO) {
        if(inStoreProductDTO == null)
            return null;
        return InStoreProduct.builder()
                .id(inStoreProductDTO.getId())
                .product(toProductConverter.convert(inStoreProductDTO.product))
                .store(toStoreConverter.convert(inStoreProductDTO.getStore()))
                .tag(toTagConverter.convert(inStoreProductDTO.getTag()))
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
