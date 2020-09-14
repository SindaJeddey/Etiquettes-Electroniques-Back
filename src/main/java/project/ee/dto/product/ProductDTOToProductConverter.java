package project.ee.dto.product;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.Category;
import project.ee.models.models.Product;
import project.ee.repositories.CategoryRepository;


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
        product.setName(productDTO.getName());
        product.setProductCode(productDTO.getProductCode());
        if(productDTO.getCategory() != null){
            Category category = categoryRepository.findByName(productDTO.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category : " + productDTO.getCategory() + " not found"));
            product.setCategory(category);
        }
        else
            product.setCategory(null);
        product.setQuantityThreshold(productDTO.getQuantityThreshold());
        product.setAddedDate(productDTO.getAddedDate());
        product.setLastModificationDate(productDTO.getLastModificationDate());
        product.setUnity(productDTO.getUnity());
        product.setDevise(productDTO.getDevise());
        product.setLongDescription(productDTO.getLongDescription());
        product.setShortDescription(productDTO.getShortDescription());
        product.setImage1(productDTO.getImage1());
        product.setImage2(productDTO.getImage2());
        product.setImage3(productDTO.getImage3());
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
