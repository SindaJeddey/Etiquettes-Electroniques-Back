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
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setQuantity(productDTO.getQuantity());
        if(productDTO.getCategoryId() != null){
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category with id: " + productDTO.getCategoryId() + " not found"));
            product.setCategory(category);
        }
        product.setQuantityThreshold(productDTO.getQuantityThreshold());
        product.setAddedDate(productDTO.getAddedDate());
        product.setLastModificationDate(productDTO.getLastModificationDate());
        product.setUnity(productDTO.getUnity());
        product.setDevise(productDTO.getDevise());
        product.setPromotion(productDTO.getPromotion());
        product.setPromotionType(productDTO.getPromotionType());
        product.setLongDescription(productDTO.getLongDescription());
        product.setShortDescription(productDTO.getShortDescription());
        product.setProductCode(productDTO.getProductCode());
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
