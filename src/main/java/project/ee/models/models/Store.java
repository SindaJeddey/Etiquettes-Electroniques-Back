package project.ee.models.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Must provide a store name")
    private String name;

    @NotEmpty(message = "Must provide a store location")
    private String location;

    @NotEmpty(message = "Must provide the store's location's ZIP code")
    private String zipCode;

    @NotEmpty(message = "Must provide a store code")
    private String storeCode ;

    @OneToMany(
            orphanRemoval = true,
            cascade = {CascadeType.REMOVE, CascadeType.MERGE},
            mappedBy = "store")
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
