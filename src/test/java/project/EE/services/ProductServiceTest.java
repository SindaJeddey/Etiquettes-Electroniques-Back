package project.EE.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.EE.dto.product.ProductDTO;
import project.EE.dto.product.ProductDTOToProductConverter;
import project.EE.dto.product.ProductToProductDTOConverter;
import project.EE.exceptions.NotFoundException;
import project.EE.models.Product;
import project.EE.models.User;
import project.EE.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductToProductDTOConverter toProductDTOConverter;

    @Mock
    ProductDTOToProductConverter toProductConverter;

    @InjectMocks
    ProductService productService;

    Product product1;
    Product product2;
    List<Product> products;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        product1 = Product.builder().id(1L).name("p1").build();
        product2 = Product.builder().id(2L).name("p2").build();
        products.add(product1);
        products.add(product2);
    }

    @Test
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> finalDtos = productService.getAllProducts();

        assertNotNull(finalDtos);
        assertEquals(2,finalDtos.size());
    }

    @Test
    void deleteProduct() throws NotFoundException {
        when(productRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(product1));
        productService.deleteProduct(1L);
        verify(productRepository,times(1)).delete(any(Product.class));
    }
}