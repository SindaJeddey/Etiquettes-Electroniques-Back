package project.EE.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.EE.dto.UserDTO;
import project.EE.exceptions.UserNotFoundException;
import project.EE.models.NotificationEmail;
import project.EE.models.UserRoles;
import project.EE.services.MailSendingService;
import project.EE.services.UserService;

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
    @PreAuthorize("permitAll()")
    public List<UserDTO> getAllOperators (){
        return userService.getAllUsers(UserRoles.OPERATOR.name());
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

    @PutMapping("/operator")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO updateOperatorRole (@RequestParam String username, @RequestBody UserDTO userDTO )
            throws UserNotFoundException {
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

    @PutMapping("/operator/update")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public UserDTO updateOperator(@RequestAttribute String user,
                                  @RequestParam String username,
                                  @RequestBody UserDTO userDTO) throws UserNotFoundException {
        if (!user.equals(username))
            throw new RuntimeException("User DATA can't be modified by another user");
        UserDTO dto =  userService.updateUser(username,userDTO,UserRoles.OPERATOR.name());
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

    @DeleteMapping("/operator")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteOperator(@RequestParam String username) throws UserNotFoundException {
        userService.deleteUser(username,UserRoles.OPERATOR.name());
    }
}