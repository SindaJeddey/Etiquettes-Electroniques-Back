package project.ee.models.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
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

    @NotEmpty(message = "Must provide the product's name")
    private String name;

    @Positive(message = "Quantity threshold must be positive")
    @NotNull
    private Long quantityThreshold;

    private LocalDate addedDate;

    private LocalDate lastModificationDate;

    @NotEmpty(message = "Must provide the product's unity")
    private String unity;

    @NotEmpty(message = "Must provide the product's devise")
    private String devise;

    @NotEmpty(message = "Must provide a long description for the product")
    @Lob
    private String longDescription;

    @NotEmpty(message = "Must provide a short description for the product")
    private String shortDescription;

    @NotEmpty(message = "Must provide a product code")
    private String productCode ;

    @NotEmpty(message = "Must provide 3 images for the product")
    private String image1;

    @NotEmpty(message = "Must provide 3 images for the product")
    private String image2;

    @NotEmpty(message = "Must provide 3 images for the product")
    private String image3;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    @NotNull(message = "Product must have a category")
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
