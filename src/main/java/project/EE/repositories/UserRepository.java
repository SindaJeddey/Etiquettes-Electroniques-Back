package project.EE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.EE.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}