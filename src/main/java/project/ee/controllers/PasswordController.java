package project.ee.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ee.dto.password.PasswordDTO;
import project.ee.dto.user.UserDTO;
import project.ee.exceptions.ResourceNotValidException;
import project.ee.models.notificationEmail.NotificationEmail;
import project.ee.services.MailSendingService;
import project.ee.services.PasswordResetService;
import project.ee.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping(PasswordController.BASE_URI)
@PreAuthorize("permitAll()")
public class PasswordController {

    public static final String BASE_URI="/api/password";

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

    @PutMapping("/token")
    public PasswordDTO passwordResetLink (@RequestBody PasswordDTO passwordDTO)  {
        String token = UUID.randomUUID().toString();
        UserDTO dto = userService.createPasswordResetToken(passwordDTO,token);
        NotificationEmail passwordReinitialization = new NotificationEmail(
                "Password Reinitialization",
                dto.getEmail(),
                String.format("Dear %s %s,%nPlease click the link below to reset your password.%n%s",
                        dto.getName(),dto.getLastName(),"http://localhost:3000/password_reset?token="+token)
        );
        mailSendingService.sendMail(passwordReinitialization);
        PasswordDTO savedPasswordDTO = new PasswordDTO();
        savedPasswordDTO.setEmail(passwordDTO.getEmail());
        savedPasswordDTO.setToken(token);
        return savedPasswordDTO;
    }

    @PutMapping("/reset")
    public String passwordReset (@RequestBody PasswordDTO passwordDTO) {
        String validity = passwordResetService.validateToken(passwordDTO.getToken());
        if (validity.equals("INVALID") || validity.equals("EXPIRED"))
            throw new ResourceNotValidException("Token "+validity);
        else {
            String email = passwordResetService.findToken(passwordDTO.getToken()).getUser().getEmail();
            UserDTO dto = userService.userPasswordReset(email,passwordDTO.getNewPassword());
            NotificationEmail passwordReinitialization = new NotificationEmail(
                    "Password Change",
                    dto.getEmail(),
                    String.format("Dear %s %s,%nYour password has been successfully reset.",
                            dto.getName(),dto.getLastName())
            );
            mailSendingService.sendMail(passwordReinitialization);
            return "SAVED";
        }
    }


}
