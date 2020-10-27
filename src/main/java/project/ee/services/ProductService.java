package project.ee.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.product.ProductDTOToProductConverter;
import project.ee.dto.product.ProductToProductDTOConverter;
import project.ee.dto.promotion.PromotionDTO;
import project.ee.dto.promotion.PromotionDTOToPromotionConverter;
import project.ee.dto.promotion.PromotionToPromotionDTOConverter;
import project.ee.exceptions.ResourceNotFoundException;
import project.ee.exceptions.ResourceNotValidException;
import project.ee.models.models.Category;
import project.ee.models.models.Product;
import project.ee.models.models.Promotion;
import project.ee.repositories.CategoryRepository;
import project.ee.repositories.ProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDTOToProductConverter toProductConverter;
    private final ProductToProductDTOConverter toProductDTOConverter;
    private final PromotionDTOToPromotionConverter toPromotionConverter;
    private final PromotionToPromotionDTOConverter toPromotionDTOConverter;

    private static final String NOT_FOUND ="%s %d not found";

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ProductDTOToProductConverter toProductConverter,
                          ProductToProductDTOConverter toProductDTOConverter,
                          PromotionDTOToPromotionConverter toPromotionConverter,
                          PromotionToPromotionDTOConverter toPromotionDTOConverter) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.toProductConverter = toProductConverter;
        this.toProductDTOConverter = toProductDTOConverter;
        this.toPromotionConverter = toPromotionConverter;
        this.toPromotionDTOConverter = toPromotionDTOConverter;
    }

    private Product fetchProduct(String id){
        return productRepository.findByProductCode(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND,"Product",id)));
    }
    public List<ProductDTO> getAllProducts (){
        return  productRepository
                .findAll()
                .stream()
                .map(toProductDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public ProductDTO getProduct(String id) throws ResourceNotFoundException {
        Product product = fetchProduct(id);
        return toProductDTOConverter.convert(product);
    }

    public ProductDTO saveNewProduct(ProductDTO productDTO) {
        Product toSave = toProductConverter.convert(productDTO);
        toSave.setAddedDate(LocalDate.now());
        toSave.setProductCode(RandomStringUtils.randomAlphabetic(10));
        toSave.setLastModificationDate(LocalDate.now());
        if (toSave.getCategory() == null)
            throw new ResourceNotValidException("Must provide a category code");
        else {
            Product saved = productRepository.save(toSave);
            saved.getCategory().addProduct(saved);
            return toProductDTOConverter.convert(saved);
        }
    }

    public ProductDTO updateProduct(String id, ProductDTO productDTO) throws ResourceNotFoundException {
        Product productToUpdate = fetchProduct(id);
        ProductDTO updatedDto;
        if(productDTO.getName() != null)
            productToUpdate.setName(productDTO.getName());
        if(productDTO.getLongDescription() != null) {
            productToUpdate.setLongDescription(productDTO.getLongDescription());
        }
        if(productDTO.getShortDescription() != null) {
            productToUpdate.setShortDescription(productDTO.getShortDescription());
        }
        if(productDTO.getUnity() != null) {
            productToUpdate.setUnity(productDTO.getUnity());
        }
        if(productDTO.getDevise() != null) {
            productToUpdate.setDevise(productDTO.getDevise());
        }
        if(productDTO.getQuantityThreshold() != null) {
            productToUpdate.setQuantityThreshold(productDTO.getQuantityThreshold());
        }
        productToUpdate.setLastModificationDate(LocalDate.now());
        if(productDTO.getCategory() != null){
            Category category = categoryRepository.findByCategoryCode(productDTO.getCategory())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND,"Category",productDTO.getCategory())));
            productToUpdate.setCategory(category);
        }
        Product saved = productRepository.save(productToUpdate);
        saved.getCategory().addProduct(saved);
        updatedDto = toProductDTOConverter.convert(saved);
        return updatedDto;
    }

    public void deleteProduct(String id) throws ResourceNotFoundException {
        Product product = fetchProduct(id);
        Category category = product.getCategory();
        category.removeProduct(product);
        productRepository.delete(product);
    }

    public void addPromotion(String id, PromotionDTO promotionDTO) throws ResourceNotFoundException {
        if(this.getCurrentPromotion(id) == null){
            Product product = fetchProduct(id);
            Promotion promotion = toPromotionConverter.convert(promotionDTO);
            product.addPromotion(promotion);
            productRepository.save(product);
        }
    }

    public PromotionDTO getCurrentPromotion(String id) throws ResourceNotFoundException {
        Product product = fetchProduct(id);
        Optional<Promotion> promo = product.getPromotions()
                .stream()
                .filter(promotion -> promotion.getPromotionEndDate().isAfter(LocalDate.now()))
                .findFirst();
        if(promo.isPresent())
            return toPromotionDTOConverter.convert(promo.get());
        else
            return null;
    }
}
