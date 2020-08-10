package project.EE.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.EE.dto.user.UserDTO;
import project.EE.exceptions.NotFoundException;
import project.EE.models.NotificationEmail;
import project.EE.models.UserRoles;
import project.EE.services.MailSendingService;
import project.EE.services.UserService;

@RestController
@RequestMapping("/api/superops")
public class SuperOperatorController {

    private final UserService userService;
    private final MailSendingService mailSendingService;


    public SuperOperatorController(UserService userService,
                                   MailSendingService mailSendingService) {
        this.userService = userService;
        this.mailSendingService = mailSendingService;
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

    @PutMapping("/superoperator")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO updateSuperOperatorRole (@RequestParam String username, @RequestBody UserDTO userDTO )
            throws NotFoundException {
        if (userDTO == null)
            throw new IllegalArgumentException("Must provide a user to update");
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

    @PutMapping("/superoperator/update")
    @PreAuthorize("hasAuthority('ROLE_SUPER_OPERATOR')")
    public UserDTO updateOperator(@RequestAttribute String user,
                                  @RequestParam String username,
                                  @RequestBody UserDTO userDTO) throws NotFoundException {
        if (!user.equals(username))
            throw new RuntimeException("User DATA can't be modified by another user");
        if (userDTO == null)
            throw new IllegalArgumentException("Must provide a user to save");
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

    @DeleteMapping("/superoperator")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteSuperOperator(@RequestParam String username) throws NotFoundException {
        userService.deleteUser(username,UserRoles.SUPER_OPERATOR.name());
    }

}
