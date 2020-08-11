package project.EE.bootstrap;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.EE.models.Category;
import project.EE.models.Product;
import project.EE.models.User;
import project.EE.models.UserRoles;
import project.EE.services.CategoryService;
import project.EE.services.ProductService;
import project.EE.services.UserService;

import java.time.LocalDate;
import java.util.Set;

@Component
@Slf4j
public class DataBootstrap implements CommandLineRunner {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public DataBootstrap(UserService userService,
                         ProductService productService,
                         CategoryService categoryService) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
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

        log.info("Loading products ...");
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
