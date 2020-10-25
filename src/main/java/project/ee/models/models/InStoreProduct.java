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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InStoreProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = "Must provide a product to store")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @NotNull(message = "Must provide a store for the product ")
    private Store store;

    @NotEmpty(message = "Must provide a code for the in store product")
    private String inStoreProductCode;

    @OneToOne(
            cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true,
            mappedBy = "product")
    @NotNull(message = "Must provide a tag for the product in store")
    private Tag tag;

    @OneToMany(
            orphanRemoval = true,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "product")
    private Set<Movement> movements = new HashSet<>();

    @NotNull(message = "Must provide an initial quantity for the in store product")
    @Positive(message = "Quantity must be positive")
    private Long quantity;

    private boolean alertThreshold;

    public void addMovement(Movement movement){
        if(this.movements == null)
            this.setMovements(new HashSet<>());
        if(this.movements.contains(movement))
            this.movements.remove(movement);
        this.movements.add(movement);
        movement.setProduct(this);
    }

    public void removeMovement(Movement movement){
        this.movements.remove(movement);
        movement.setProduct(null);
    }

    public Category getCategory(){
        return this.product.getCategory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InStoreProduct that = (InStoreProduct) o;
        return Objects.equals(id, that.id);
    }

}
