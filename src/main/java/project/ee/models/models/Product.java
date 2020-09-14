package project.ee.models.models;

import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
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

    @NotEmpty
    private String name;

    @Positive
    private Long quantityThreshold;

    private LocalDate addedDate;

    private LocalDate lastModificationDate;

    @NotNull
    private String unity;

    @NotNull
    private String devise;

    @NotEmpty
    @Lob
    private String longDescription;

    @NotEmpty
    private String shortDescription;

    private String productCode ;

    @NotNull
    private String image1;

    @NotNull
    private String image2;

    @NotNull
    private String image3;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(
            orphanRemoval = true,
            mappedBy = "product")
    private Set<InStoreProduct> inStoreProducts ;

    @OneToMany(
            orphanRemoval = true,
            mappedBy = "product",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private Set<Promotion> promotions;

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

    public void addPromotion(Promotion promotion){
        if (this.promotions == null)
            this.setPromotions(new HashSet<>());
        if (this.promotions.contains(promotion))
            this.promotions.remove(promotion);
        this.promotions.add(promotion);
        promotion.setProduct(this);
    }

    public void removePromotion(Promotion promotion){
        this.promotions.remove(promotion);
        promotion.setProduct(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) ;
    }

}
