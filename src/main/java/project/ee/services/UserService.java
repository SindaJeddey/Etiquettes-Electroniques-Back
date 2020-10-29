package project.ee.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.ee.dto.password.PasswordDTO;
import project.ee.dto.user.UserDTO;
import project.ee.dto.user.UserDTOToUserConverter;
import project.ee.dto.user.UserToUserDTOConverter;
import project.ee.exceptions.ResourceNotFoundException;
import project.ee.exceptions.ResourceNotValidException;
import project.ee.models.authentication.PasswordResetToken;
import project.ee.models.authentication.User;
import project.ee.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    private User fetchUser (String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username "+username+" not found"));
    }

    public UserDTO saveNewUser(UserDTO userDTO, String role){
        User toSave = toUserConverter.convert(userDTO);
        toSave.setRole(role);
        String password = UUID.randomUUID().toString();
        toSave.setPassword(passwordEncoder.encode(password));
        User saved = userRepository.save(toSave);
        UserDTO savedDto = toUserDTOConverter.convert(saved);
        savedDto.setPassword(null);
        return savedDto;
    }

    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserDTO updateUserRole (String username, UserDTO userDTO) {
        User toUpdate = fetchUser(username);

        UserDTO savedDto;
        if (toUpdate.getRole().equals(userDTO.getRole())){
            savedDto = toUserDTOConverter.convert(toUpdate);
            savedDto.setPassword(null);
            return savedDto;
        }

        toUpdate.setRole(userDTO.getRole());
        User updated = userRepository.save(toUpdate);
        savedDto = toUserDTOConverter.convert(updated);
        savedDto.setPassword(null);
        return savedDto;
    }

    public void deleteUser(String username, String role) {
        User toDelete = fetchUser(username);
        if (!toDelete.getRole().equals(role))
            throw new ResourceNotValidException("Incompatible username"+username+" with role "+role);
        userRepository.delete(toDelete);
    }

    public UserDTO updateUser(String username, UserDTO userDTO, String role) {
        User toUpdate = fetchUser(username);
        if (!toUpdate.getRole().equalsIgnoreCase(role))
            throw new ResourceNotFoundException("Can't find Username"+username+" with role"+role);
        if(userDTO.getUsername()!=null)
            throw new ResourceNotValidException("Username is unique. You can't change it.");
        if(userDTO.getName()!=null)
            toUpdate.setName(userDTO.getName());
        if(userDTO.getLastName()!=null)
            toUpdate.setLastName(userDTO.getLastName());
        if(userDTO.getBirthday()!=null)
            toUpdate.setBirthday(userDTO.getBirthday());
        if(userDTO.getEmail()!=null)
            toUpdate.setEmail(userDTO.getEmail());
        if(userDTO.getPassword()!=null)
            toUpdate.setPassword(userDTO.getPassword());
        User updated = userRepository.save(toUpdate);
        return toUserDTOConverter.convert(updated);
    }

    public List<UserDTO> getAllUsers(String role){
        return userRepository.findAllByRole(role)
                .stream()
                .map(toUserDTOConverter::convert)
                .map(userDTO -> {
                    userDTO.setPassword(null);
                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    public List<String> fetchAllUsernames(String role){
        return userRepository.findAllByRole(role)
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(toUserDTOConverter::convert)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find user with email "+email));
    }

    public UserDTO createPasswordResetToken(PasswordDTO passwordDTO, String token) {
        User user = userRepository.findByEmail(passwordDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Can't find user with email "+passwordDTO.getEmail()));
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        LocalDate expiryDate = LocalDate.now().plusDays(1);
        resetToken.setExpiryDate(expiryDate);
        user.addResetToken(resetToken);
        User saved = userRepository.save(user);
        return toUserDTOConverter.convert(saved);
    }

    public UserDTO userPasswordReset(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Email "+email+"not found"));
        user.setPassword(passwordEncoder.encode(password));
        User saved = userRepository.save(user);
        return toUserDTOConverter.convert(saved);
    }

    public UserDTO getUser(String username, String role) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getRole().equals(role))
                .map(toUserDTOConverter::convert)
                .orElseThrow(()-> new ResourceNotFoundException("Username "+username+"not found"));

    }
}
