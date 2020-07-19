package project.EE.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.EE.models.User;
import project.EE.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        user = User.builder().username("sady").id(1L).build();
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
}