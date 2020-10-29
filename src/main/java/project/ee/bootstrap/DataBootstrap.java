package project.ee.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.ee.models.models.*;
import project.ee.models.authentication.User;
import project.ee.models.authentication.UserRoles;
import project.ee.services.CategoryService;
import project.ee.services.StoreService;
import project.ee.services.UserService;

import java.time.LocalDate;
import java.util.HashSet;

@Component
@Slf4j
public class DataBootstrap implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final StoreService storeService;

    public DataBootstrap(UserService userService,
                         CategoryService categoryService,
                         StoreService storeService) {
        this.userService = userService;
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
                .unity("cm")
                .devise("Dinar")
                .productCode("1")
//                .productCode(RandomStringUtils.randomAlphabetic(10))
                .longDescription("Long description prod1")
                .shortDescription("Short description prod1")
                .image1("image1 prod1")
                .image2("image2 prod1")
                .image3("image3 prod1")
                .build();

        Promotion promotion = Promotion.builder()
                .promotion("20 %")
                .promotionType("Reduction")
                .promoCode(RandomStringUtils.randomAlphabetic(10))
                .promotionEndDate(LocalDate.of(2020,12,30))
                .build();
        product1.addPromotion(promotion);

        Product product2 = Product.builder()
                .name("Product 2")
                .quantityThreshold(1000L)
                .addedDate(LocalDate.now())
                .unity("l")
                .devise("Dollar")
                .productCode("2")
//                .productCode(RandomStringUtils.randomAlphabetic(10))
                .longDescription("Long description prod2")
                .shortDescription("Short description prod2")
                .image1("image1 prod2")
                .image2("image2 prod2")
                .image3("image3 prod2")
                .build();

        Category cat1 = Category.builder()
                .name("Cat 1")
                .categoryCode("1")
//                .categoryCode(RandomStringUtils.randomAlphabetic(5))
                .products(new HashSet<>())
                .build();
        cat1.addProduct(product1);

        Category cat2 = Category.builder()
                .name("Cat 2")
                .categoryCode("2")
//                .categoryCode(RandomStringUtils.randomAlphabetic(5))
                .products(new HashSet<>())
                .build();
        cat2.addProduct(product2);

        Store store1 = Store.builder()
                .name("Store 1")
                .location("Aouina")
                .zipCode("2045")
                .storeCode("1")
//                .storeCode(RandomStringUtils.randomAlphabetic(10))
                .inStoreProducts(new HashSet<>())
                .build();

        Store store2 = Store.builder()
                .name("Store 2")
                .location("Lac1")
                .storeCode("2")
//                .storeCode(RandomStringUtils.randomAlphabetic(10))
                .zipCode("2044")
                .inStoreProducts(new HashSet<>())
                .build();

        InStoreProduct inStoreProduct = InStoreProduct.builder()
                .quantity(1500L)
                .inStoreProductCode(RandomStringUtils.randomAlphabetic(10))
                .build();
        product1.addInStoreProduct(inStoreProduct);

        Tag tag = Tag.builder()
                .product(inStoreProduct)
                .name("Tag1")
                .type("Tag type1")
                .tagCode(RandomStringUtils.randomAlphabetic(10))
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

        categoryService.saveCategory(cat1);
        categoryService.saveCategory(cat2);
        log.info("LOADING CATEGORIES ...");
        log.info("LOADING PRODUCTS ...");
        log.info("LOADING PROMOTIONS ...");
        storeService.save(store1);
        storeService.save(store2);
        log.info("LOADING STORES ...");
        log.info("LOADING IN STORE PRODUCTS...");
        log.info("LOADING MOVEMENTS ...");
        log.info("LOADING TAGS...");

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

        log.info("LOADING USERS ...");
    }
}
