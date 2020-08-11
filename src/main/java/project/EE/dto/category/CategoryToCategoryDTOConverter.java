package project.EE.dto.category;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.EE.dto.product.ProductDTO;
import project.EE.dto.product.ProductToProductDTOConverter;
import project.EE.models.Category;

import java.util.stream.Collectors;

@Component
public class CategoryToCategoryDTOConverter implements Converter<Category,CategoryDTO> {

    private final ProductToProductDTOConverter toProductDTOConverter;

    public CategoryToCategoryDTOConverter(ProductToProductDTOConverter toProductDTOConverter) {
        this.toProductDTOConverter = toProductDTOConverter;
    }

    @Override
    public CategoryDTO convert(Category category) {
        if(category == null)
            return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setProductSet(category.getProductSet()
                .stream()
                .map(product -> {
                    ProductDTO productDTO = toProductDTOConverter.convert(product);
                    productDTO.setCategoryId(category.getId());
                    return productDTO;
                })
                .collect(Collectors.toSet()));
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
