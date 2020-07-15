package project.EE.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.EE.models.User;
import project.EE.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("sady");
    }


    @Test
    void saveNewUser() {
        when(userRepository.save(any())).thenReturn(user);
        User toSave = new User();
        toSave.setUsername("ghj");
        User saved = userService.saveNewUser(toSave);

        assertNotNull(saved);
        assertEquals(saved.getId(),user.getId());
        assertEquals(saved.getUsername(),user.getUsername());
    }
}