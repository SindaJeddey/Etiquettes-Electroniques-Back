package project.ee.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.ee.models.models.Category;
import project.ee.models.models.InStoreProduct;
import project.ee.models.models.Product;
import project.ee.models.authentication.User;
import project.ee.models.authentication.UserRoles;
import project.ee.models.models.Store;
import project.ee.repositories.InStoreProductRepository;
import project.ee.services.CategoryService;
import project.ee.services.ProductService;
import project.ee.services.StoreService;
import project.ee.services.UserService;

import java.time.LocalDate;
import java.util.HashSet;

@Component
@Slf4j
public class DataBootstrap implements CommandLineRunner {

    private final InStoreProductRepository inStoreProductRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final StoreService storeService;

    public DataBootstrap(InStoreProductRepository inStoreProductRepository, UserService userService,
                         ProductService productService,
                         CategoryService categoryService,
                         StoreService storeService) {
        this.inStoreProductRepository = inStoreProductRepository;
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.storeService = storeService;
    }


    @Override
    public void run(String... args) throws Exception {
       loadUsers();
       loadProductsAndCategories();
    }


    private void loadProductsAndCategories() {
        Product product1 = Product.builder()
                .name("Product 1")
                .quantity(300L)
                .addedDate(LocalDate.now())
                .build();

        Product product2 = Product.builder()
                .name("Product 2")
                .quantity(100L)
                .addedDate(LocalDate.now())
                .build();

        Category cat1 = Category.builder()
                .name("Cat 1")
                .products(new HashSet<>())
                .build();
        cat1.addProduct(product1);
        product1.setCategory(cat1);
        categoryService.saveCategory(cat1);

        Category cat2 = Category.builder()
                .name("Cat 2")
                .products(new HashSet<>())
                .build();
        cat2.addProduct(product2);
        product2.setCategory(cat2);
        categoryService.saveCategory(cat2);

        log.info("Loading categories ...");

        log.info("Loading products ...");

        Store store1 = Store.builder()
                .name("Store 1")
                .location("aouina")
                .zipCode("2045")
                .inStoreProducts(new HashSet<>())
                .build();
        storeService.save(store1);

        Store store2 = Store.builder()
                .name("Store 2")
                .location("lac")
                .zipCode("2044")
                .inStoreProducts(new HashSet<>())
                .build();
        storeService.save(store2);

        InStoreProduct inStoreProduct = InStoreProduct.builder()
                .product(product1)
                .store(store1)
                .build();
        store1.addInStoreProduct(inStoreProduct);
        storeService.save(store1);


        log.info("Loading stores ...");
    }

    private void loadUsers() {
        User admin = User.builder()
                .username("admin")
                .password("123")
                .role(UserRoles.ADMIN.name())
                .email("admin@admin.com")
                .build();
        userService.saveUser(admin);

        User operator = User.builder()
                .username("operator")
                .password("123")
                .role(UserRoles.OPERATOR.name())
                .email("operator@operator.com")
                .build();
        userService.saveUser(operator);

        User superOperator = User.builder()
                .username("super_operator")
                .password("123")
                .role(UserRoles.SUPER_OPERATOR.name())
                .email("supop@supop.com")
                .build();
        userService.saveUser(superOperator);

        log.info("Users loaded in database ...");
    }
}
