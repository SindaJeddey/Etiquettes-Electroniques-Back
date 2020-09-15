package project.ee.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.ee.models.models.*;
import project.ee.models.authentication.User;
import project.ee.models.authentication.UserRoles;
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
                .quantityThreshold(500L)
                .addedDate(LocalDate.now())
                .productCode(RandomStringUtils.randomAlphabetic(10))
                .build();

        Promotion promotion = Promotion.builder()
                .promotion("20 %")
                .promotionType("Reduction")
                .promoCode(RandomStringUtils.randomAlphabetic(10))

                .promotionEndDate(LocalDate.of(2020,10,1))
                .build();
        product1.addPromotion(promotion);

        Product product2 = Product.builder()
                .name("Product 2")
                .quantityThreshold(1000L)
                .productCode(RandomStringUtils.randomAlphabetic(10))
                .addedDate(LocalDate.now())
                .build();

        Category cat1 = Category.builder()
                .name("Cat 1")
                .categoryCode(RandomStringUtils.randomAlphabetic(5))
                .products(new HashSet<>())
                .build();
        cat1.addProduct(product1);
        product1.setCategory(cat1);
        categoryService.saveCategory(cat1);

        Category cat2 = Category.builder()
                .name("Cat 2")
                .categoryCode(RandomStringUtils.randomAlphabetic(5))
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
                .storeCode(RandomStringUtils.randomAlphabetic(10))
                .inStoreProducts(new HashSet<>())
                .build();
        storeService.save(store1);

        Store store2 = Store.builder()
                .name("Store 2")
                .location("lac")
                .storeCode(RandomStringUtils.randomAlphabetic(10))
                .zipCode("2044")
                .inStoreProducts(new HashSet<>())
                .build();
        storeService.save(store2);

        InStoreProduct inStoreProduct = InStoreProduct.builder()
                .product(product1)
                .quantity(1500L)
                .store(store1)
                .inStoreProductCode(RandomStringUtils.randomAlphabetic(10))
                .build();
        Tag tag = Tag.builder()
                .product(inStoreProduct)
                .build();
        inStoreProduct.setTag(tag);

        Movement movement = Movement.builder()
                .movementDate(LocalDate.now())
                .type("IN")
                .movementCode(RandomStringUtils.randomAlphabetic(10))
                .quantity(500L)
                .product(inStoreProduct)
                .build();
        inStoreProduct.addMovement(movement);
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
