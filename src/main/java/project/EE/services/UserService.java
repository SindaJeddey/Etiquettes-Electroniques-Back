package project.EE.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.EE.dto.user.UserDTO;
import project.EE.dto.user.UserDTOToUserConverter;
import project.EE.dto.user.UserToUserDTOConverter;
import project.EE.exceptions.NotFoundException;
import project.EE.models.PasswordResetToken;
import project.EE.models.User;
import project.EE.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOToUserConverter toUserConverter;
    private final UserToUserDTOConverter toUserDTOConverter;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserDTOToUserConverter toUserConverter,
                       UserToUserDTOConverter toUserDTOConverter,
                       PasswordResetService passwordResetService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.toUserConverter = toUserConverter;
        this.toUserDTOConverter = toUserDTOConverter;
        this.passwordResetService = passwordResetService;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserDTO updateUserRole (String username, UserDTO userDTO) throws NotFoundException {
        User toUpdate = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username "+username+" not found"));

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

    public void deleteUser(String username, String role) throws NotFoundException {
        User toDelete = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username "+username +"not found"));
        if (!toDelete.getRole().equals(role))
            throw new UsernameNotFoundException("Incompatible username "+username+" with role "+role);
        userRepository.delete(toDelete);
        log.info("User "+username+" deleted");
    }

    public UserDTO updateUser(String username, UserDTO userDTO, String role) throws NotFoundException {
        User toUpdate = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username "+username+" not found"));
        if (!toUpdate.getRole().equals(role))
            throw new NotFoundException("Can't find Username "+username+" with role"+role);
        if(userDTO.getUsername()!=null)
            throw new RuntimeException("Username is unique. You can't change it.");
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
        UserDTO savedDto = toUserDTOConverter.convert(updated);
        return savedDto;
    }

    public List<UserDTO> getAllUsers(String role){
        List<UserDTO> dtos = userRepository.findAllByRole(role)
                .stream()
                .map(user -> toUserDTOConverter.convert(user))
                .map(userDTO -> {
                    userDTO.setPassword(null);
                    return userDTO;
                })
                .collect(Collectors.toList());
        return dtos;
    }


    public UserDTO getUserByEmail(String email) throws NotFoundException {
        UserDTO dto = userRepository.findByEmail(email)
                .map(user -> toUserDTOConverter.convert(user))
                .orElseThrow(() -> new NotFoundException("Can't find user with email "+email));
        return dto;
    }

    public void createPasswordResetToken(UserDTO dto, String token) {
        User user = toUserConverter.convert(dto);
        PasswordResetToken resetToken = passwordResetService.saveNewToken(token,user);
        user.setResetToken(resetToken);
        userRepository.save(user);
    }

    public UserDTO userPasswordReset(String email, String password) throws NotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("Email "+email+"not found"));
        user.setPassword(passwordEncoder.encode(password));
        User saved = userRepository.save(user);
        UserDTO savedDto = toUserDTOConverter.convert(saved);
        return savedDto;
    }

}
