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

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.REMOVE, CascadeType.MERGE}, mappedBy = "store")
    private Set<InStoreProduct> inStoreProducts;

    public void addInStoreProduct(InStoreProduct inStoreProduct){
        if(this.inStoreProducts == null)
            this.setInStoreProducts(new HashSet<>());
        if(this.inStoreProducts.contains(inStoreProduct))
            this.inStoreProducts.remove(inStoreProduct);
        this.inStoreProducts.add(inStoreProduct);
        inStoreProduct.setStore(this);
    }

    public void removeInStoreProduct(InStoreProduct inStoreProduct){
            this.inStoreProducts.remove(inStoreProduct);
            inStoreProduct.setStore(null);

    }

}
