package project.EE.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.EE.dto.PasswordDTO;
import project.EE.dto.UserDTO;
import project.EE.exceptions.UserNotFoundException;
import project.EE.models.NotificationEmail;
import project.EE.models.User;
import project.EE.services.MailSendingService;
import project.EE.services.PasswordResetService;
import project.EE.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/password")
@PreAuthorize("permitAll()")
public class PasswordController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final MailSendingService mailSendingService;

    public PasswordController(UserService userService,
                              PasswordResetService passwordResetService,
                              MailSendingService mailSendingService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
        this.mailSendingService = mailSendingService;
    }

    @PostMapping("/token")
    public PasswordDTO passwordResetLink (@RequestBody PasswordDTO passwordDTO) throws UserNotFoundException {
        UserDTO dto = userService.getUserByEmail(passwordDTO.getEmail());
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetToken(dto,token);

        NotificationEmail password_reinitialization = new NotificationEmail(
                "Password Reinitialization",
                dto.getEmail(),
                String.format("Dear %s %s,\nPlease click the link below to reset your password.\n%s",
                        dto.getName(),dto.getLastName(),"http://localhost:3000/password_reset?token="+token)
        );
        mailSendingService.sendMail(password_reinitialization);

        PasswordDTO savedPasswordDTO = new PasswordDTO();
        savedPasswordDTO.setEmail(passwordDTO.getEmail());
        savedPasswordDTO.setToken(token);
        return savedPasswordDTO;
    }

    @PostMapping("/reset")
    public String passwordReset (@RequestBody PasswordDTO passwordDTO) throws UserNotFoundException {
        String validity = passwordResetService.validateToken(passwordDTO.getToken());
        if (validity.equals("INVALID") || validity.equals("EXPIRED"))
            throw new RuntimeException("Token "+validity);
        else {
            String email = passwordResetService.findToken(passwordDTO.getToken()).getUser().getEmail();
            UserDTO dto = userService.userPasswordReset(email,passwordDTO.getNewPassword());
            NotificationEmail password_reinitialization = new NotificationEmail(
                    "Password Change",
                    dto.getEmail(),
                    String.format("Dear %s %s,\nYour password has been successfully reset.",
                            dto.getName(),dto.getLastName())
            );
            mailSendingService.sendMail(password_reinitialization);
            return "SAVED";
        }
    }


}
