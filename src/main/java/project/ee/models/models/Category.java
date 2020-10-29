package project.ee.models.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotEmpty(message = "Must provide a category name")
    private String name;

    @NotEmpty(message = "Must provide category code")
    private String categoryCode;

    @OneToMany(
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
