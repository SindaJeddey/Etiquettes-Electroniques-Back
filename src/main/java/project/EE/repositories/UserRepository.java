package project.EE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.EE.models.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
