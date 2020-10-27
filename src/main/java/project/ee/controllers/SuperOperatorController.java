package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.user.UserDTO;
import project.ee.models.notificationEmail.NotificationEmail;
import project.ee.models.authentication.UserRoles;
import project.ee.services.MailSendingService;
import project.ee.services.UserService;

import java.util.List;

@RestController
@RequestMapping(SuperOperatorController.BASE_URI)
@Validated
public class SuperOperatorController {

    public static final String BASE_URI="/api/super-operators";
    private final UserService userService;
    private final MailSendingService mailSendingService;


    public SuperOperatorController(UserService userService,
                                   MailSendingService mailSendingService) {
        this.userService = userService;
        this.mailSendingService = mailSendingService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserDTO> getAllSuperOperators (){
        return userService.getAllUsers(UserRoles.SUPER_OPERATOR.name());
    }

    @GetMapping("/{username}")
    @PreAuthorize("permitAll()")
    public UserDTO getSuperOperator(@PathVariable String username){
        return userService.getUser(username, UserRoles.SUPER_OPERATOR.name());
    }

    @GetMapping("/usernames")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<String> fetchAllUsernames(){
        return userService.fetchAllUsernames(UserRoles.SUPER_OPERATOR.name());
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO newSuperOperator(@RequestBody UserDTO dto){
        UserDTO savedDto = userService.saveNewUser(dto,UserRoles.SUPER_OPERATOR.name());
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
    public UserDTO updateSuperOperatorRole (@PathVariable String username, @RequestBody UserDTO userDTO ){
        UserDTO updatedDto = userService.updateUserRole(username,userDTO);
        NotificationEmail email = new NotificationEmail(
                "Account Modification",
                updatedDto.getEmail(),
                String.format("Dear %s %s,\nYour account has been modified." +
                                "\nModified credentials are:\nRole: %s",
                        updatedDto.getName(),updatedDto.getLastName(),updatedDto.getRole())
        );
        mailSendingService.sendMail(email);
        return updatedDto;
    }

    @PutMapping("/update/{username}")
    @PreAuthorize("hasAuthority('ROLE_SUPER_OPERATOR')")
    public UserDTO updateSuperOperator(@PathVariable String username,
                                  @RequestBody UserDTO userDTO){
        UserDTO dto =  userService.updateUser(username,userDTO,UserRoles.SUPER_OPERATOR.name());
        NotificationEmail email = new NotificationEmail(
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
        mailSendingService.sendMail(email);
        dto.setPassword(null);
        return dto;
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteSuperOperator(@PathVariable String username){
        userService.deleteUser(username,UserRoles.SUPER_OPERATOR.name());
    }

}
