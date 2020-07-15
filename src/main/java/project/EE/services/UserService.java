package project.EE.services;

import org.springframework.stereotype.Service;
import project.EE.models.User;
import project.EE.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveNewUser (User user){
        User savedUser = userRepository.save(user);
        return savedUser;
    }
}
