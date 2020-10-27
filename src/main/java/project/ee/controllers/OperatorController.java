package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.user.UserDTO;
import project.ee.models.notificationEmail.NotificationEmail;
import project.ee.models.authentication.UserRoles;
import project.ee.services.MailSendingService;
import project.ee.services.UserService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(OperatorController.BASE_URI)
@Validated
public class OperatorController {

    public static final String BASE_URI="/api/operators";

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
    public UserDTO getOperator(@PathVariable String username) {
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
    public UserDTO updateOperatorRole (@PathVariable @NotEmpty String username,
                                       @RequestBody @NotNull UserDTO userDTO ){
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
                                  @RequestBody UserDTO userDTO) {
        UserDTO dto =  userService.updateUser(username,userDTO,UserRoles.OPERATOR.name());
        NotificationEmail accountModification = new NotificationEmail(
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
        mailSendingService.sendMail(accountModification);
        dto.setPassword(null);
        return dto;
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteOperator(@PathVariable String username){
        userService.deleteUser(username,UserRoles.OPERATOR.name());
    }
}