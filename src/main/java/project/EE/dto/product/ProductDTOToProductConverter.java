package project.EE.dto.product;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import project.EE.exceptions.NotFoundException;
import project.EE.models.models.Category;
import project.EE.models.models.Product;
import project.EE.repositories.CategoryRepository;


@Component
public class ProductDTOToProductConverter implements Converter<ProductDTO, Product> {

    private final CategoryRepository categoryRepository;

    public ProductDTOToProductConverter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @SneakyThrows
    @Override
    public Product convert(ProductDTO productDTO) {
        if(productDTO == null)
            return null;
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setQuantity(productDTO.getQuantity());
        if(productDTO.getCategoryId() != null){
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category with id: " + productDTO.getCategoryId() + " not found"));
            product.setCategory(category);
        }
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
