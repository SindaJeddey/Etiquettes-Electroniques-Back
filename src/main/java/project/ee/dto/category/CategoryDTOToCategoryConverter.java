package project.ee.dto.category;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.models.models.Category;

@Component
public class CategoryDTOToCategoryConverter implements Converter<CategoryDTO, Category> {

    @Override
    public Category convert(CategoryDTO categoryDTO) {
        if (categoryDTO == null)
            return null;
        Category category = new Category();
        category.setCategoryCode(categoryDTO.getCategoryCode());
        category.setName(categoryDTO.getName());
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
