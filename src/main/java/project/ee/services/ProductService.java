package project.ee.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.product.ProductDTOToProductConverter;
import project.ee.dto.product.ProductToProductDTOConverter;
import project.ee.exceptions.NotFoundException;
import project.ee.models.models.Category;
import project.ee.models.models.Product;
import project.ee.models.models.Store;
import project.ee.repositories.CategoryRepository;
import project.ee.repositories.ProductRepository;
import project.ee.repositories.StoreRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final ProductDTOToProductConverter toProductConverter;
    private final ProductToProductDTOConverter toProductDTOConverter;

    private static String NOT_FOUND ="%s %d not found";

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          StoreRepository storeRepository,
                          ProductDTOToProductConverter toProductConverter,
                          ProductToProductDTOConverter toProductDTOConverter) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
        this.toProductConverter = toProductConverter;
        this.toProductDTOConverter = toProductDTOConverter;
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

    public ProductDTO getProduct(Long id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Product",id)));
        return toProductDTOConverter.convert(product);
    }

    public ProductDTO saveNewProduct(ProductDTO productDTO) {
        Product toSave = toProductConverter.convert(productDTO);
        toSave.setAddedDate(LocalDate.now());
        toSave.setLastModificationDate(LocalDate.now());
        if (toSave.getCategory() == null)
            throw new RuntimeException("Must provide a category for this product");
        else {
            Product saved = productRepository.save(toSave);
            saved.getCategory().addProduct(saved);
            return toProductDTOConverter.convert(saved);
        }
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws NotFoundException {
        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Product",id)));
        Product updates = toProductConverter.convert(productDTO);
        ProductDTO updatedDto;
        if(updates.getName() != null)
            productToUpdate.setName(updates.getName());
        if(updates.getQuantity() != null)
            productToUpdate.setQuantity(updates.getQuantity());
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

    public void deleteProduct(Long id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Product",id)));
        Category category = product.getCategory();
        category.removeProduct(product);
        productRepository.delete(product); //see if this instruction is needed
    }
}
