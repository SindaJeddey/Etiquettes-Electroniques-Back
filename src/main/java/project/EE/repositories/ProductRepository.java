package project.EE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.EE.models.models.Product;


public interface ProductRepository extends JpaRepository<Product,Long> {
}
