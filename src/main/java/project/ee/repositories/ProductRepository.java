package project.ee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import project.ee.models.models.Product;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByProductCode(String code);
}
