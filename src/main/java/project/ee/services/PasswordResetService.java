package project.ee.services;

import org.springframework.stereotype.Service;
import project.ee.models.authentication.PasswordResetToken;
import project.ee.models.authentication.User;
import project.ee.repositories.PasswordResetTokenRepository;
import project.ee.repositories.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository,
                                UserRepository userRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
    }

    public PasswordResetToken saveNewToken (String token, User user){
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        LocalDate expiryDate = LocalDate.now().plusDays(1);
        resetToken.setExpiryDate(expiryDate);
        PasswordResetToken saved = passwordResetTokenRepository.save(resetToken);
        return saved;
    }

    public String validateToken(String token) {
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepository
                .findByToken(token);
        if(passwordResetTokenOptional.isPresent()){
            PasswordResetToken finalToken = passwordResetTokenOptional.get();
            if(finalToken.getExpiryDate().isBefore(LocalDate.now()))
                return "EXPIRED";
            User user = finalToken.getUser();
            if(user == null)
                return "INVALID";
            Optional<User> supposedUser = userRepository.findByUsername(user.getUsername());
            if(!supposedUser.isPresent() || !user.equals(supposedUser.get()))
                return "INVALID";
            return "VALID";
        }
        return "INVALID";
    }

    public PasswordResetToken findToken(String token){
        PasswordResetToken finalToken =passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
        return finalToken;
    }
}
