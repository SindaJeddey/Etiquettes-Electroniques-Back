package project.ee.dto.category;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;
import project.ee.models.models.Category;

@Component
public class CategoryToCategoryDTOConverter implements Converter<Category,CategoryDTO> {

    @Override
    public CategoryDTO convert(Category category) {
        if(category == null)
            return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryCode(category.getCategoryCode());
        dto.setName(category.getName());
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
