package project.ee.bootstrap;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.ee.models.models.Category;
import project.ee.models.models.Product;
import project.ee.models.authentication.User;
import project.ee.models.authentication.UserRoles;
import project.ee.models.models.Store;
import project.ee.services.CategoryService;
import project.ee.services.ProductService;
import project.ee.services.StoreService;
import project.ee.services.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class DataBootstrap implements CommandLineRunner {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final StoreService storeService;

    public DataBootstrap(UserService userService,
                         ProductService productService,
                         CategoryService categoryService,
                         StoreService storeService) {
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

        Set<Product> products = Sets.newHashSet(product1,product2);

        Category cat1 = Category.builder()
                .name("Cat 1")
                .build();

        cat1.setProductSet(products);
        categoryService.saveCategory(cat1);

        product1.setCategory(cat1);
        productService.saveProduct(product1);
        product2.setCategory(cat1);
        productService.saveProduct(product2);

        Category cat2 = Category.builder()
                .name("Cat 2")
                .build();
        categoryService.saveCategory(cat2);

        Store store1 = Store.builder()
                .name("Store 1")
                .location("aouina")
                .zipCode("2045")
                .categories(Sets.newHashSet(cat1,cat2))
                .products(products)
                .build();
        storeService.save(store1);

        Set<Store> stores = new HashSet<>();
        stores.add(store1);
        cat1.setStores(stores);
        categoryService.saveCategory(cat1);
        cat2.setStores(stores);
        categoryService.saveCategory(cat2);

        log.info("Loading categories ...");

        product1.setStores(stores);
        product2.setStores(stores);
        productService.saveProduct(product1);
        productService.saveProduct(product2);

        log.info("Loading products ...");

        Store store2 = Store.builder()
                .name("Store 2")
                .location("lac")
                .zipCode("2044")
                .build();
        storeService.save(store2);

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
