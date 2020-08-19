package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.Store;

public interface StoreRepository extends JpaRepository<Store,Long> {
}
