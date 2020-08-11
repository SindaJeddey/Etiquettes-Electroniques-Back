package project.EE.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.EE.dto.product.ProductDTO;
import project.EE.dto.product.ProductDTOToProductConverter;
import project.EE.dto.product.ProductToProductDTOConverter;
import project.EE.exceptions.NotFoundException;
import project.EE.models.Category;
import project.EE.models.Product;
import project.EE.repositories.CategoryRepository;
import project.EE.repositories.ProductRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDTOToProductConverter toProductConverter;
    private final ProductToProductDTOConverter toProductDTOConverter;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ProductDTOToProductConverter toProductConverter,
                          ProductToProductDTOConverter toProductDTOConverter) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.toProductConverter = toProductConverter;
        this.toProductDTOConverter = toProductDTOConverter;
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public List<ProductDTO> getAllProducts (){
        List<ProductDTO> dtos = productRepository
                .findAll()
                .stream()
                .map(product -> toProductDTOConverter.convert(product))
                .collect(Collectors.toList());
        return dtos;
    }

    public ProductDTO getProduct(Long id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id: "+id+" not found"));
        ProductDTO dto = toProductDTOConverter.convert(product);
        return dto;
    }

    public ProductDTO save(ProductDTO productDTO) throws NotFoundException {
        Product toSave = toProductConverter.convert(productDTO);
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category with id: "+productDTO.getCategoryId()+" not found"));
        toSave.setCategory(category);
        Product saved = productRepository.save(toSave);
        Set<Product> products = category.getProductSet();
        products.add(saved);
        category.setProductSet(products);
        categoryRepository.save(category);
        ProductDTO savedDto = toProductDTOConverter.convert(saved);
        return savedDto;
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws NotFoundException {
        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id: "+id+" not found"));
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
        Product saved = productRepository.save(productToUpdate);
        updatedDto = toProductDTOConverter.convert(saved);
        return updatedDto;
    }

    public void deleteProduct(Long id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id: "+id+" not found"));
        Category category = product.getCategory();
        Set<Product> products = category.getProductSet();
        products.remove(product);
        category.setProductSet(products);
        System.out.println(category.getProductSet());;
        categoryRepository.save(category);
        productRepository.delete(product);
    }
}
