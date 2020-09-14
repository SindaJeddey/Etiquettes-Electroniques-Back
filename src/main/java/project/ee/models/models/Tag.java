package project.ee.models.models;

import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

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

    @NotEmpty
    private String type;

    private String tagCode;

    @NotEmpty
    private String name;

    @OneToOne
    private InStoreProduct product;

    @ManyToOne
    private Transmitter transmitter;

}
