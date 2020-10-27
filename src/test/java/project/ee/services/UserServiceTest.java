package project.ee.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.ee.exceptions.ResourceNotFoundException;
import project.ee.models.authentication.User;
import project.ee.models.authentication.UserRoles;
import project.ee.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder().username("sady").id(1L).role(UserRoles.ADMIN.name()).build();
    }


    @Test
    void saveNewUser() {
        when(userRepository.save(any())).thenReturn(user);
        User toSave = User.builder().username("fds").build();
        User saved = userService.saveUser(toSave);

        assertNotNull(saved);
        assertEquals(saved.getId(),user.getId());
        assertEquals(saved.getUsername(),user.getUsername());
    }

    @Test
    void deleteUser() throws ResourceNotFoundException {
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.ofNullable(user));
        userService.deleteUser("admin",UserRoles.ADMIN.name());
        verify(userRepository,times(1)).delete(any(User.class));
    }
}