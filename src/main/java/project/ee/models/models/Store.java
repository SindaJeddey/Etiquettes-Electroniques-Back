package project.ee.models.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    private String zipCode;

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<InStoreProduct> inStoreProducts = new HashSet<>();

    public void addInStoreProduct(InStoreProduct inStoreProduct){
        this.inStoreProducts.add(inStoreProduct);
    }

    public void removeInStoreProduct(InStoreProduct inStoreProduct){
        this.inStoreProducts.remove(inStoreProduct);
    }

}
