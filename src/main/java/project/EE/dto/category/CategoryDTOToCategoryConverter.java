package project.EE.dto.category;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.EE.dto.product.ProductDTOToProductConverter;
import project.EE.models.Category;

import java.util.stream.Collectors;

@Component
public class CategoryDTOToCategoryConverter implements Converter<CategoryDTO, Category> {
    private final ProductDTOToProductConverter productDTOToProductConverter;

    public CategoryDTOToCategoryConverter(ProductDTOToProductConverter productDTOToProductConverter) {
        this.productDTOToProductConverter = productDTOToProductConverter;
    }

    @Override
    public Category convert(CategoryDTO categoryDTO) {
        if (categoryDTO == null)
            return null;
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setProductSet(categoryDTO.getProductSet()
                .stream()
                .map(productDTO -> productDTOToProductConverter.convert(productDTO))
                .collect(Collectors.toSet()));
        return category;
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
