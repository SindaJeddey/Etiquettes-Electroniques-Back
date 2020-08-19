package project.ee.dto.product;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.models.models.Product;

@Component
public class ProductToProductDTOConverter implements Converter<Product, ProductDTO> {
    @Override
    public ProductDTO convert(Product product) {
        if (product == null)
            return null;
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setQuantity(product.getQuantity());
        dto.setCategoryId(product.getCategory().getId());
        return dto;
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
