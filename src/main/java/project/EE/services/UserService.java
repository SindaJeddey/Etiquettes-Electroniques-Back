package project.EE.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.EE.dto.UserDTO;
import project.EE.dto.UserDTOToUserConverter;
import project.EE.dto.UserToUserDTOConverter;
import project.EE.models.User;
import project.EE.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOToUserConverter toUserConverter;
    private final UserToUserDTOConverter toUserDTOConverter;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserDTOToUserConverter toUserConverter,
                       UserToUserDTOConverter toUserDTOConverter,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.toUserConverter = toUserConverter;
        this.toUserDTOConverter = toUserDTOConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO saveNewUser(UserDTO userDTO, String role){
        User toSave = toUserConverter.convert(userDTO);
        toSave.setRole(role);
        String password = toSave.getPassword();
        toSave.setPassword(passwordEncoder.encode(password));
        User saved = userRepository.save(toSave);
        UserDTO savedDto = toUserDTOConverter.convert(saved);
        savedDto.setPassword(null);
        return savedDto;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
}
