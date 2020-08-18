package project.EE.models.models;

import lombok.*;
import javax.persistence.*;
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
    private String name;
    private Long quantity;
    private LocalDate addedDate;

    @ManyToOne
    private Category category;

    @ManyToMany(mappedBy = "products")
    private Set<Store> stores = new HashSet<>();

}
