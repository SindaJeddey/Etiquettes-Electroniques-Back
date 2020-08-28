package project.ee.models.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String code;
    private String name;

    @OneToOne
    private InStoreProduct product;

    @ManyToOne
    private Transmitter transmitter;

}
