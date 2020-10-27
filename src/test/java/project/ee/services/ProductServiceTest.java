package project.ee.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.ee.dto.product.ProductDTO;
import project.ee.dto.product.ProductToProductDTOConverter;
import project.ee.exceptions.ResourceNotFoundException;
import project.ee.models.models.Category;
import project.ee.models.models.Product;
import project.ee.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductToProductDTOConverter toProductDTOConverter;

    @InjectMocks
    ProductService productService;

    Category cat1;
    Product product1;
    Product product2;
    List<Product> products;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        cat1 = Category.builder().name("cat1").build();
        product1 = Product.builder().id(1L).name("p1").build();
        product2 = Product.builder().id(2L).name("p2").build();
        products.add(product1);
        products.add(product2);
        cat1.setProducts(new HashSet<>(products));
        product1.setCategory(cat1);
    }

    @Test
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> finalDtos = productService.getAllProducts();

        assertNotNull(finalDtos);
        assertEquals(2,finalDtos.size());
    }

    @Test
    void deleteProduct() throws ResourceNotFoundException {
        when(productRepository.findByProductCode(anyString())).thenReturn(java.util.Optional.ofNullable(product1));
        productService.deleteProduct("8512351");
        assertEquals(1,cat1.getProducts().size());
    }
}