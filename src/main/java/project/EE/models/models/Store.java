package project.EE.models.models;

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

    @ManyToMany
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    private Set<Product> products = new HashSet<>();
}
