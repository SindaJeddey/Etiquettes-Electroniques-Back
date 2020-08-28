package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.Tag;

public interface TagRepository extends JpaRepository<Tag,Long> {
}
