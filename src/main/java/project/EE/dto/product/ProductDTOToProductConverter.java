package project.EE.dto.product;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.EE.models.Product;


@Component
public class ProductDTOToProductConverter implements Converter<ProductDTO, Product> {
    @Override
    public Product convert(ProductDTO productDTO) {
        if(productDTO == null)
            return null;
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setQuantity(productDTO.getQuantity());
        return product;
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
