package project.ee.models.models;

import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    private String categoryCode;

    @OneToMany(
            orphanRemoval = true,
            cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST},
            mappedBy = "category")
    private Set<Product> products = new HashSet<>();

    public void addProduct(Product product){
        if(this.products == null)
            this.setProducts(new HashSet<>());
        if(this.products.contains(product))
            this.products.remove(product);
        this.products.add(product);
        product.setCategory(this);
    }

    public void removeProduct(Product product){
        this.products.remove(product);
        product.setCategory(null);
    }

}
