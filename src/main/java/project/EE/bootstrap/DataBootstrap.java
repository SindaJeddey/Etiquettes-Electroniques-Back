package project.EE.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.EE.models.User;
import project.EE.models.UserRoles;
import project.EE.services.UserService;

@Component
@Slf4j
public class DataBootstrap implements CommandLineRunner {

    private final UserService userService;

    public DataBootstrap(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void run(String... args) throws Exception {
       loadUsers();
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
