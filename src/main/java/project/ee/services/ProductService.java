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
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.Category;
import project.ee.models.models.Product;
import project.ee.models.models.Promotion;
import project.ee.repositories.CategoryRepository;
import project.ee.repositories.ProductRepository;
import project.ee.repositories.StoreRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDTOToProductConverter toProductConverter;
    private final ProductToProductDTOConverter toProductDTOConverter;
    private final PromotionDTOToPromotionConverter toPromotionConverter;
    private final PromotionToPromotionDTOConverter toPromotionDTOConverter;

    private static String NOT_FOUND ="%s %d not found";

    public ProductService(ProductRepository productRepository,
                          ProductDTOToProductConverter toProductConverter,
                          ProductToProductDTOConverter toProductDTOConverter,
                          PromotionDTOToPromotionConverter toPromotionConverter,
                          PromotionToPromotionDTOConverter toPromotionDTOConverter) {
        this.productRepository = productRepository;
        this.toProductConverter = toProductConverter;
        this.toProductDTOConverter = toProductDTOConverter;
        this.toPromotionConverter = toPromotionConverter;
        this.toPromotionDTOConverter = toPromotionDTOConverter;
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public List<ProductDTO> getAllProducts (){
        return  productRepository
                .findAll()
                .stream()
                .map(toProductDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public ProductDTO getProduct(String id) throws NotFoundException {
        Product product = productRepository.findByProductCode(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Product",id)));
        return toProductDTOConverter.convert(product);
    }

    public ProductDTO saveNewProduct(ProductDTO productDTO) {
        Product toSave = toProductConverter.convert(productDTO);
        toSave.setAddedDate(LocalDate.now());
        toSave.setProductCode(RandomStringUtils.randomAlphabetic(10));
        toSave.setLastModificationDate(LocalDate.now());
        if (toSave.getCategory() == null)
            throw new RuntimeException("Must provide a category for this product");
        else {
            Product saved = productRepository.save(toSave);
            saved.getCategory().addProduct(saved);
            return toProductDTOConverter.convert(saved);
        }
    }

    public ProductDTO updateProduct(String id, ProductDTO productDTO) throws NotFoundException {
        Product productToUpdate = productRepository.findByProductCode(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Product",id)));
        Product updates = toProductConverter.convert(productDTO);
        ProductDTO updatedDto;
        if(updates.getName() != null)
            productToUpdate.setName(updates.getName());
        if(updates.getLongDescription() != null) {
            productToUpdate.setLongDescription(updates.getLongDescription());
        }
        if(updates.getShortDescription() != null) {
            productToUpdate.setShortDescription(updates.getShortDescription());
        }
        if(updates.getUnity() != null) {
            productToUpdate.setUnity(updates.getUnity());
        }
        if(updates.getDevise() != null) {
            productToUpdate.setDevise(updates.getDevise());
        }
        if(updates.getQuantityThreshold() != null) {
            productToUpdate.setQuantityThreshold(updates.getQuantityThreshold());
        }
        productToUpdate.setLastModificationDate(LocalDate.now());
        if(updates.getCategory() != null){
            productToUpdate.setCategory(updates.getCategory());
        }
        Product saved = productRepository.save(productToUpdate);
        saved.getCategory().addProduct(saved);
        updatedDto = toProductDTOConverter.convert(saved);
        return updatedDto;
    }

    public void deleteProduct(String id) throws NotFoundException {
        Product product = productRepository.findByProductCode(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Product",id)));
        Category category = product.getCategory();
        category.removeProduct(product);
        productRepository.delete(product); //see if this instruction is needed
    }

    public void addPromotion(String id, PromotionDTO promotionDTO) throws NotFoundException {
        if (id == null)
            throw new RuntimeException("Must provide product to add promotion to");
        if(this.getCurrentPromotion(id) == null){
            Product product = productRepository.findByProductCode(id)
                    .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Product",id)));
            Promotion promotion = toPromotionConverter.convert(promotionDTO);
            product.addPromotion(promotion);
            productRepository.save(product);
        }
    }

    public PromotionDTO getCurrentPromotion(String id) throws NotFoundException {
        Product product = productRepository.findByProductCode(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Product",id)));
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
