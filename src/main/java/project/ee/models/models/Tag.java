package project.ee.models.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    @NotEmpty(message = "Must provide a tag type")
    private String type;

    @NotEmpty(message = "Must provide a tag code")
    private String tagCode;

    @NotEmpty(message = "Must provide a tag name")
    private String name;

    @OneToOne
    @NotNull(message = "Must provide an in store product for the tag")
    private InStoreProduct product;

    @ManyToOne
    //@NotNull(message = "Must provide the transmitter for the tag")
    private Transmitter transmitter;

}
