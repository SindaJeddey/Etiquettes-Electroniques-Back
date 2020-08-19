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

    public ProductDTO save(ProductDTO productDTO) throws NotFoundException {
        Product toSave = toProductConverter.convert(productDTO);
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Category",productDTO.getCategoryId())));
        toSave.setCategory(category);
        if(toSave.getAddedDate() == null)
            toSave.setAddedDate(LocalDate.now());
        else
            toSave.setLastModificationDate(LocalDate.now());
        Product saved = productRepository.save(toSave);
        Set<Product> products = category.getProductSet();
        products.add(saved);
        category.setProductSet(products);
        categoryRepository.save(category);
        return toProductDTOConverter.convert(saved);
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
        if(updates.getCategory() != null){
            Category category = updates.getCategory();
            productToUpdate.setCategory(category);
            Set<Product> products = category.getProductSet();
            products.add(productToUpdate);
            category.setProductSet(products);
            categoryRepository.save(category);
        }
        productToUpdate.setLastModificationDate(LocalDate.now());
        Product saved = productRepository.save(productToUpdate);
        updatedDto = toProductDTOConverter.convert(saved);
        return updatedDto;
    }

    public void deleteProduct(Long id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND,"Product",id)));

        //Deleting product from all the stores
        Set<Store> stores = product.getStores();
        stores.forEach(store -> {
            Set<Product> products = store.getProducts();
            products.remove(product);
            store.setProducts(products);
            storeRepository.save(store);
        });

        //Deleting the product from its category
        Category category = product.getCategory();
        Set<Product> categoryProduct = category.getProductSet();
        categoryProduct.remove(product);
        category.setProductSet(categoryProduct);
        categoryRepository.save(category);

        //Deleting the product
        productRepository.delete(product);
    }
}
