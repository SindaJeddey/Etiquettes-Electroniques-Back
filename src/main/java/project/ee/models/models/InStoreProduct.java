package project.ee.models.models;

import lombok.*;

import javax.persistence.*;
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
    private Product product;

    @ManyToOne
    private Store store;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Tag tag;

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Movement> movements = new HashSet<>();

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
