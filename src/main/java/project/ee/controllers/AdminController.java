package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.user.UserDTO;
import project.ee.models.authentication.UserRoles;
import project.ee.models.notificationEmail.NotificationEmail;
import project.ee.services.MailSendingService;
import project.ee.services.UserService;

import java.util.List;

@RestController
@RequestMapping(AdminController.BASE_URI)
@PreAuthorize("permitAll()")
public class AdminController {

    public static final String BASE_URI="/api/admins";

    private final UserService userService;
    private final MailSendingService mailSendingService;

    public AdminController(UserService userService,
                           MailSendingService mailSendingService) {
        this.userService = userService;
        this.mailSendingService = mailSendingService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserDTO> getAllAdmins (){
        return userService.getAllUsers(UserRoles.ADMIN.name());
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO getAdmin(@PathVariable String username) {
        return userService.getUser(username, UserRoles.ADMIN.name());
    }

    @GetMapping("/usernames")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<String> fetchAllUsernames(){
        return userService.fetchAllUsernames(UserRoles.ADMIN.name());
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO newAdmin(@RequestBody UserDTO dto){
        UserDTO savedDto = userService.saveNewUser(dto,UserRoles.ADMIN.name());
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

    @PutMapping("/update/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO updateAdmin(@PathVariable String username,
                               @RequestBody UserDTO userDTO){
        UserDTO dto =  userService.updateUser(username,userDTO,UserRoles.ADMIN.name());
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
}
