package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.authentication.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByRole(String role);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String token);
}
