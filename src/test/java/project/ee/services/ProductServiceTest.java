package project.ee.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.ArrayList;
import java.util.HashSet;
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
    StoreRepository storeRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ProductToProductDTOConverter toProductDTOConverter;

    @Mock
    ProductDTOToProductConverter toProductConverter;

    @InjectMocks
    ProductService productService;

    Category cat1;
    Product product1;
    Product product2;
    List<Product> products;
    Store store1;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        cat1 = Category.builder().name("cat1").productSet(new HashSet<>()).stores(new HashSet<>()).build();
        product1 = Product.builder().id(1L).name("p1").stores(new HashSet<>()).category(cat1).build();
        product2 = Product.builder().id(2L).name("p2").build();
        products.add(product1);
        products.add(product2);
        store1 = Store.builder().name("store1").id(1L).products(new HashSet<>()).build();
        store1.getProducts().add(product1);
        product1.getStores().add(store1);
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
        verify(storeRepository,times(1)).save(any(Store.class));
        verify(categoryRepository,times(1)).save(any(Category.class));
        verify(productRepository,times(1)).delete(any(Product.class));
    }

}