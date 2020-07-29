package project.EE.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.EE.dto.UserDTO;
import project.EE.exceptions.UserNotFoundException;
import project.EE.models.NotificationEmail;
import project.EE.services.MailSendingService;
import project.EE.services.UserService;

@RestController
@RequestMapping("/api/password")
@PreAuthorize("permitAll()")
public class PasswordController {
    private final UserService userService;
    private final MailSendingService mailSendingService;

    public PasswordController(UserService userService,
                              MailSendingService mailSendingService) {
        this.userService = userService;
        this.mailSendingService = mailSendingService;
    }

}
