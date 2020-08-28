package project.ee.models.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
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

    @OneToOne
    private Tag tag;

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Movement> movements = new HashSet<>();

    public void addMovement(Movement movement){
        this.movements.add(movement);
    }
}
