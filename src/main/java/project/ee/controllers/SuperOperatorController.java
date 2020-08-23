package project.ee.controllers;

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
@RequestMapping("/api/super-operators")
public class SuperOperatorController {

    private final UserService userService;
    private final MailSendingService mailSendingService;


    public SuperOperatorController(UserService userService,
                                   MailSendingService mailSendingService) {
        this.userService = userService;
        this.mailSendingService = mailSendingService;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<UserDTO> getAllSuperOperators (){
        return userService.getAllUsers(UserRoles.SUPER_OPERATOR.name());
    }

    @GetMapping("/{id}")
    public UserDTO getSuperOperator(@PathVariable Long id) throws NotFoundException {
        return userService.getUser(id, UserRoles.SUPER_OPERATOR.name());
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO newSuperOperator(@RequestBody UserDTO dto){
        if (dto == null)
            throw new IllegalArgumentException("Must provide a user to save");
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO updateSuperOperatorRole (@PathVariable Long id, @RequestBody UserDTO userDTO )
            throws NotFoundException {
        if (userDTO == null)
            throw new IllegalArgumentException("Must provide a user to update");
        UserDTO updatedDto = userService.updateUserRole(id,userDTO);
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

    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ROLE_SUPER_OPERATOR')")
    public UserDTO updateOperator(@RequestAttribute String user,
                                  @PathVariable Long id,
                                  @RequestBody UserDTO userDTO) throws NotFoundException {
//        if (!user.equals(username))
//            throw new RuntimeException("User DATA can't be modified by another user");
        if (userDTO == null)
            throw new IllegalArgumentException("Must provide a user to save");
        UserDTO dto =  userService.updateUser(id,userDTO,UserRoles.SUPER_OPERATOR.name());
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteSuperOperator(@PathVariable Long id) throws NotFoundException {
        userService.deleteUser(id,UserRoles.SUPER_OPERATOR.name());
    }

}
