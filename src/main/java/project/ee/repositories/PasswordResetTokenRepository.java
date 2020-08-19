package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.authentication.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
