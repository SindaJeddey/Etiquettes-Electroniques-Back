package project.ee.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.user.UserDTO;
import project.ee.exceptions.NotFoundException;
import project.ee.models.notificationEmail.NotificationEmail;
import project.ee.models.authentication.UserRoles;
import project.ee.services.MailSendingService;
import project.ee.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/operators")
@Slf4j
public class OperatorController {

    private final UserService userService;
    private final MailSendingService mailSendingService;

    public OperatorController(UserService userService,
                              MailSendingService mailSendingService) {
        this.userService = userService;
        this.mailSendingService = mailSendingService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserDTO> getAllOperators (){
        return userService.getAllUsers(UserRoles.OPERATOR.name());
    }

    @GetMapping("/{username}")
    @PreAuthorize("permitAll()")
    public UserDTO getOperator(@PathVariable String username) throws NotFoundException {
        return userService.getUser(username, UserRoles.OPERATOR.name());
    }

    @GetMapping("/usernames")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<String> fetchAllUsernames(){
        return userService.fetchAllUsernames(UserRoles.OPERATOR.name());
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO newOperator(@RequestBody UserDTO dto){
        if (dto == null)
            throw new IllegalArgumentException("Must provide a user to save");
        UserDTO savedDto = userService.saveNewUser(dto,UserRoles.OPERATOR.name());
        NotificationEmail email = new NotificationEmail(
                "Account Activation",
                dto.getEmail(),
                String.format("Dear %s %s,\nYour account has been created and activated." +
                                "\nCredentials are:\nUsername: %s\nPassword: %s",
                        savedDto.getName(),savedDto.getLastName(),savedDto.getUsername(),dto.getPassword())
        );
        mailSendingService.sendMail(email);
        return savedDto;
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO updateOperatorRole (@PathVariable String username, @RequestBody UserDTO userDTO )
            throws NotFoundException {
        if (userDTO == null)
            throw new IllegalArgumentException("Must provide a user to save");
        UserDTO updatedDto = userService.updateUserRole(username,userDTO);
        NotificationEmail email = new NotificationEmail(
                "Account Modification",
                updatedDto.getEmail(),
                String.format("Dear %s %s,\nYour account has been modified." +
                                "\nYour new role: %s",
                        updatedDto.getName(),updatedDto.getLastName(),updatedDto.getRole())
        );
        mailSendingService.sendMail(email);
        return updatedDto;
    }

    @PutMapping("/update/{username}")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public UserDTO updateOperator(@PathVariable String username,
                                  @RequestBody UserDTO userDTO) throws NotFoundException {
        if (userDTO == null)
            throw new IllegalArgumentException("Must provide a user to save");
        UserDTO dto =  userService.updateUser(username,userDTO,UserRoles.OPERATOR.name());
        NotificationEmail account_modification = new NotificationEmail(
                "Account Modification",
                dto.getEmail(),
                String.format("Dear %s %s,\nYour account has been modified." +
                                "\nYour new profile credentials are:\nName: %s" +
                                "\nLast name: %s\nUsername: %s\nPassword: %s" +
                                "\nEmail: %s\nDate of birth: %s",
                        dto.getName(),dto.getLastName(),dto.getName(),
                        dto.getLastName(),dto.getUsername(),
                        dto.getPassword(),dto.getEmail(),dto.getBirthday())
        );
        mailSendingService.sendMail(account_modification);
        dto.setPassword(null);
        return dto;
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteOperator(@PathVariable String username) throws NotFoundException {
        userService.deleteUser(username,UserRoles.OPERATOR.name());
    }
}