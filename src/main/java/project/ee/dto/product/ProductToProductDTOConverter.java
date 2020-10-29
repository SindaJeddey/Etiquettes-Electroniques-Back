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
        dto.setProductCode(product.getProductCode());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory().getCategoryCode());
        dto.setQuantityThreshold(product.getQuantityThreshold());
        dto.setAddedDate(product.getAddedDate());
        dto.setLastModificationDate(product.getLastModificationDate());
        dto.setUnity(product.getUnity());
        dto.setDevise(product.getDevise());
        dto.setLongDescription(product.getLongDescription());
        dto.setShortDescription(product.getShortDescription());
        dto.setImage1(product.getImage1());
        dto.setImage2(product.getImage2());
        dto.setImage3(product.getImage3());
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
