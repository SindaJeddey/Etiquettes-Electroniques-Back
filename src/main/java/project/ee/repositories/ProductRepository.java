package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ee.models.models.Product;


public interface ProductRepository extends JpaRepository<Product,Long> {
}
