package project.ee.models.models;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long quantity;

    private Long quantityThreshold;

    private LocalDate addedDate;

    private LocalDate lastModificationDate;

    private String unity;

    private String devise;

    private String promotion;

    private String promotionType;

    private LocalDate promotionEndDate;

    private String longDescription;

    private String shortDescription;

    private String productCode;

    private byte[] image1;

    private byte[] image2;

    private byte[] image3;

    private byte[] barcode;

    @ManyToOne()
    private Category category;

    @OneToMany(orphanRemoval = true)
    private Set<InStoreProduct> inStoreProducts ;

    public void addInStoreProduct(InStoreProduct inStoreProduct){
        if (this.inStoreProducts == null)
            this.setInStoreProducts(new HashSet<>());
        if(this.inStoreProducts.contains(inStoreProduct))
            this.inStoreProducts.remove(inStoreProduct);
        this.inStoreProducts.add(inStoreProduct);
        inStoreProduct.setProduct(this);
    }

    public void removeInStoreProduct(InStoreProduct inStoreProduct){
            this.inStoreProducts.remove(inStoreProduct);
            inStoreProduct.setStore(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) ;
    }

}
