package project.EE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.EE.models.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
}
