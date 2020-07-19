package project.EE.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.EE.dto.UserDTO;
import project.EE.models.NotificationEmail;
import project.EE.models.UserRoles;
import project.EE.services.MailSendingService;
import project.EE.services.UserService;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {

    private final UserService userService;
    private final MailSendingService mailSendingService;

    public OperatorController(UserService userService,
                              MailSendingService mailSendingService) {
        this.userService = userService;
        this.mailSendingService = mailSendingService;
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
}