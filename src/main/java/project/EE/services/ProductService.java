package project.EE.services;

import org.springframework.stereotype.Service;
import project.EE.dto.product.ProductDTO;
import project.EE.dto.product.ProductDTOToProductConverter;
import project.EE.dto.product.ProductToProductDTOConverter;
import project.EE.exceptions.NotFoundException;
import project.EE.models.Product;
import project.EE.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDTOToProductConverter toProductConverter;
    private final ProductToProductDTOConverter toProductDTOConverter;

    public ProductService(ProductRepository productRepository,
                          ProductDTOToProductConverter toProductConverter,
                          ProductToProductDTOConverter toProductDTOConverter) {
        this.productRepository = productRepository;
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

    public ProductDTO save(ProductDTO productDTO) {
        Product toSave = toProductConverter.convert(productDTO);
        Product saved = productRepository.save(toSave);
        ProductDTO savedDto = toProductDTOConverter.convert(saved);
        return savedDto;
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws NotFoundException {
        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id: "+id+" not found"));
        Product updates = toProductConverter.convert(productDTO);
        if(updates.getName() != null)
            productToUpdate.setName(updates.getName());
        if(updates.getQuantity() != null)
            productToUpdate.setQuantity(updates.getQuantity());
        ProductDTO updatedDto = toProductDTOConverter.convert(productToUpdate);
        return updatedDto;
    }

    public void deleteProduct(Long id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id: "+id+" not found"));
        productRepository.delete(product);
    }
}
